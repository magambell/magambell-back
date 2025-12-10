package com.magambell.server.payment.app.port.in;

/**
 * 결제 성공 처리 UseCase
 * - webhook, redirect, 테스트용 API 등에서 공통으로 사용
 */
public interface PaymentCompleteUseCase {

    /**
     * 결제 성공 처리
     * - Payment 상태를 PAID로 변경
     * - Order 상태를 PAID로 변경
     * - 사장님에게 알림 발송
     *
     * @param merchantUid 주문 고유 ID (ORDER_{orderId})
     */
    void completePayment(String merchantUid);
}
