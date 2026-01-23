package com.magambell.server.payment.app.service;

import static com.magambell.server.payment.domain.enums.PaymentStatus.CANCELLED;
import static com.magambell.server.payment.domain.enums.PaymentStatus.FAILED;
import static com.magambell.server.payment.domain.enums.PaymentStatus.PAID;
import static com.magambell.server.payment.domain.enums.PaymentStatus.READY;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.payment.app.port.in.PaymentCompleteUseCase;
import com.magambell.server.payment.app.port.in.PaymentUseCase;
import com.magambell.server.payment.app.port.in.request.PaymentRedirectPaidServiceRequest;
import com.magambell.server.payment.app.port.in.request.PortOneWebhookServiceRequest;
import com.magambell.server.payment.app.port.out.PaymentQueryPort;
import com.magambell.server.payment.app.port.out.PortOnePort;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.enums.PaymentCompletionType;
import com.magambell.server.payment.infra.PortOnePaymentResponse;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import com.magambell.server.stock.app.port.in.StockUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentService implements PaymentUseCase {

    public static final String MERCHANT_UID_PREFIX = "ORDER_";

    private final PortOnePort portOnePort;
    private final PaymentQueryPort paymentQueryPort;
    private final StockUseCase stockUseCase;
    private final NotificationUseCase notificationUseCase;
    private final PaymentCompleteUseCase paymentCompleteUseCase;

    @Transactional
    @Override
    public void redirectPaid(final PaymentRedirectPaidServiceRequest request) {
        log.info("PortOne redirect 결제 완료 처리 시작 - paymentId: {}", request.paymentId());
        
        PortOnePaymentResponse portOnePaymentResponse = portOnePort.getPaymentById(request.paymentId());
        Payment payment = paymentQueryPort.findByMerchantUidWithLockAndRelations(portOnePaymentResponse.id());
        
        if (payment.getPaymentCompletionType() == PaymentCompletionType.WEBHOOK) {
            log.info("Payment {} already processed via WEBHOOK, ignoring redirect", payment.getId());
            return;
        }
        
        if (payment.isPaid()) {
            log.info("Payment {} already processed, ignoring redirect", payment.getId());
            return;
        }
        
        // PortOne 검증
        validatePaid(portOnePaymentResponse, payment);
        log.info("PortOne 검증 성공 - merchantUid: {}, amount: {}", portOnePaymentResponse.id(), portOnePaymentResponse.amount().total());
        
        // Payment 엔티티에 PortOne 정보 설정
        payment.paid(portOnePaymentResponse, PaymentCompletionType.REDIRECT);
        
        // 재고 차감 포함 완전한 결제 완료 처리는 이미 payment.paid()에서 order.paid() 호출로 처리됨
        // 하지만 재고 차감은 별도로 필요
        log.info("재고 차감 및 알림 발송 시작 - orderId: {}", payment.getOrder().getId());
        
        // 재고 차감 처리 추가
        payment.getOrder().getOrderGoodsList().forEach(orderGoods -> {
            var goods = orderGoods.getGoods();
            var stock = goods.getStock();
            var stockHistory = stock.recordDecrease(goods, orderGoods.getQuantity());
            log.info("재고 차감 완료 - goodsId: {}, {} -> {}", goods.getId(), 
                    stockHistory.getBeforeQuantity(), stockHistory.getResultQuantity());
        });
        
        notificationUseCase.notifyPaidOrder(payment.getOrderStoreOwner());
        log.info("PortOne redirect 결제 완료 처리 성공 - merchantUid: {}, orderId: {}", 
                portOnePaymentResponse.id(), payment.getOrder().getId());
    }

    @Transactional
    @Override
    public void webhook(final PortOneWebhookServiceRequest request) {
        PortOnePaymentResponse portOnePaymentResponse = portOnePort.getPaymentById(request.paymentId());
        Payment payment = paymentQueryPort.findByMerchantUidWithLockAndRelations(portOnePaymentResponse.id());

        switch (request.paymentStatus()) {
            case PAID -> {
                log.info("PortOne webhook PAID 처리 시작 - paymentId: {}", request.paymentId());
                
                if (payment.getPaymentCompletionType() == PaymentCompletionType.REDIRECT) {
                    log.info("Payment {} already processed via redirect, ignoring WEBHOOK", payment.getId());
                    return;
                }
                
                if (payment.isPaid()) {
                    log.info("Payment {} already processed, ignoring WEBHOOK", payment.getId());
                    return;
                }
                
                validatePaid(portOnePaymentResponse, payment);
                log.info("PortOne webhook 검증 성공 - merchantUid: {}, amount: {}", portOnePaymentResponse.id(), portOnePaymentResponse.amount().total());
                
                payment.paid(portOnePaymentResponse, PaymentCompletionType.WEBHOOK);
                
                // 재고 차감 처리 추가
                log.info("재고 차감 시작 - orderId: {}", payment.getOrder().getId());
                payment.getOrder().getOrderGoodsList().forEach(orderGoods -> {
                    var goods = orderGoods.getGoods();
                    var stock = goods.getStock();
                    var stockHistory = stock.recordDecrease(goods, orderGoods.getQuantity());
                    log.info("재고 차감 완료 - goodsId: {}, {} -> {}", goods.getId(), 
                            stockHistory.getBeforeQuantity(), stockHistory.getResultQuantity());
                });
                
                notificationUseCase.notifyPaidOrder(payment.getOrderStoreOwner());
                log.info("PortOne webhook PAID 처리 성공 - merchantUid: {}, orderId: {}", 
                        portOnePaymentResponse.id(), payment.getOrder().getId());
            }
            case CANCELLED -> {
                if (payment.getPaymentStatus() == CANCELLED) {
                    log.info("Payment {} already processed CANCEL", payment.getId());
                    return;
                }
                validateCancelled(portOnePaymentResponse, payment);
                payment.hookCancel();

                restoreStockIfNecessary(payment);
            }
            case FAILED -> {
                validateFailed(portOnePaymentResponse, payment);
                payment.failed();

                restoreStockIfNecessary(payment);
            }
            default -> {
            }
        }
    }

    private void validatePaid(final PortOnePaymentResponse response, final Payment payment) {
        if (payment.isPaid()) {
            throw new InvalidRequestException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }
        if (response.status() != PAID) {
            throw new InvalidRequestException(ErrorCode.INVALID_PAYMENT_STATUS_PAID);
        }
        if (!payment.getAmount().equals(response.amount().total())) {
            throw new InvalidRequestException(ErrorCode.TOTAL_PRICE_NOT_EQUALS);
        }
    }

    private void validateCancelled(final PortOnePaymentResponse response, final Payment payment) {
        if (!payment.isPaid()) {
            throw new InvalidRequestException(ErrorCode.PAYMENT_NOT_ALREADY_PROCESSED);
        }
        if (response.status() != CANCELLED) {
            throw new InvalidRequestException(ErrorCode.INVALID_PAYMENT_STATUS_CANCEL);
        }
    }

    private void validateFailed(final PortOnePaymentResponse response, final Payment payment) {
        if (payment.getPaymentStatus() != READY) {
            throw new InvalidRequestException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }
        if (response.status() != FAILED) {
            throw new InvalidRequestException(ErrorCode.INVALID_PAYMENT_STATUS_CANCEL);
        }
    }

    private void restoreStockIfNecessary(final Payment payment) {
        stockUseCase.restoreStockIfNecessary(payment);
    }

}
