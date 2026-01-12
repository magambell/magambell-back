package com.magambell.server.payment.app.service;

import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.order.domain.entity.OrderGoods;
import com.magambell.server.payment.app.port.in.PaymentCompleteUseCase;
import com.magambell.server.payment.app.port.out.PaymentQueryPort;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.stock.domain.entity.Stock;
import com.magambell.server.stock.domain.entity.StockHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 완료 처리 서비스
 * - PortOne webhook, redirect, 테스트용 API 등에서 공통으로 사용
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentCompleteService implements PaymentCompleteUseCase {

    private final PaymentQueryPort paymentQueryPort;
    private final NotificationUseCase notificationUseCase;

    @Override
    public void completePayment(final String merchantUid) {
        log.info("결제 완료 처리 시작 - merchantUid: {}", merchantUid);
        
        // 1. Payment 조회 (Lock) - Port는 Payment 직접 반환 (Adapter에서 Optional 처리)
        Payment payment = paymentQueryPort.findByMerchantUidWithLockAndRelations(merchantUid);
        log.info("결제 정보 조회 완료 - paymentId: {}, orderId: {}, status: {}", 
                payment.getId(), payment.getOrder().getId(), payment.getPaymentStatus());

        // 2. 이미 처리된 결제인지 확인
        if (payment.isPaid()) {
            log.info("이미 처리된 결제 - paymentId: {}, merchantUid: {}", payment.getId(), merchantUid);
            return;
        }

        // 3. 재고 차감 처리
        log.info("재고 차감 시작 - orderId: {}", payment.getOrder().getId());
        for (OrderGoods orderGoods : payment.getOrder().getOrderGoodsList()) {
            Goods goods = orderGoods.getGoods();
            Stock stock = goods.getStock();
            StockHistory stockHistory = stock.recordDecrease(goods, orderGoods.getQuantity());
            log.info("재고 차감 완료 - goodsId: {}, {} -> {}", goods.getId(), 
                    stockHistory.getBeforeQuantity(), stockHistory.getResultQuantity());
        }

        // 4. Order 상태를 PAID로 변경 및 테스트용 transactionId 설정
        log.info("주문 상태 PAID로 변경 - orderId: {}", payment.getOrder().getId());
        payment.getOrder().paid();
        
        // 테스트 환경용 transactionId 설정 (실제 PortOne API 호출 없이)
        // test_ prefix를 사용하여 테스트 결제임을 명시
        String testTransactionId = "test_" + merchantUid;
        payment.setTestTransactionId(testTransactionId);
        log.info("테스트 transactionId 설정 완료: {}", testTransactionId);
        
        // 5. 사장님에게 알림 발송
        log.info("사장님 알림 전송 시작 - 사장님 수: {}", payment.getOrderStoreOwner().size());
        notificationUseCase.notifyPaidOrder(payment.getOrderStoreOwner());

        log.info("결제 완료 처리 성공 - merchantUid: {}, orderId: {}", 
                merchantUid, payment.getOrder().getId());
    }
}
