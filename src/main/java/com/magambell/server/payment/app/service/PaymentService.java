package com.magambell.server.payment.app.service;

import static com.magambell.server.payment.domain.enums.PaymentStatus.CANCELLED;
import static com.magambell.server.payment.domain.enums.PaymentStatus.FAILED;
import static com.magambell.server.payment.domain.enums.PaymentStatus.PAID;
import static com.magambell.server.payment.domain.enums.PaymentStatus.READY;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.payment.app.port.in.PaymentUseCase;
import com.magambell.server.payment.app.port.in.request.PaymentRedirectPaidServiceRequest;
import com.magambell.server.payment.app.port.in.request.PortOneWebhookServiceRequest;
import com.magambell.server.payment.app.port.out.PaymentQueryPort;
import com.magambell.server.payment.app.port.out.PortOnePort;
import com.magambell.server.payment.domain.model.Payment;
import com.magambell.server.payment.infra.PortOnePaymentResponse;
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

    @Transactional
    @Override
    public void redirectPaid(final PaymentRedirectPaidServiceRequest request) {
        PortOnePaymentResponse portOnePaymentResponse = portOnePort.getPaymentById(request.paymentId());
        Payment payment = paymentQueryPort.findByMerchantUidJoinOrder(portOnePaymentResponse.id());
        validatePaid(portOnePaymentResponse, payment);
        payment.paid(portOnePaymentResponse);
    }

    @Transactional
    @Override
    public void webhook(final PortOneWebhookServiceRequest request) {
        PortOnePaymentResponse portOnePaymentResponse = portOnePort.getPaymentById(request.paymentId());
        Payment payment = paymentQueryPort.findByMerchantUidJoinOrder(portOnePaymentResponse.id());

        switch (request.paymentStatus()) {
            case PAID -> {
                validatePaid(portOnePaymentResponse, payment);
                payment.paid(portOnePaymentResponse);
            }
            case CANCELLED -> {
                validateCancelled(portOnePaymentResponse, payment);
                payment.cancel();

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

        if (response.method().provider() == null) {
            throw new InvalidRequestException(ErrorCode.INVALID_EASY_PAY_PROVIDER);
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
