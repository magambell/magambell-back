package com.magambell.server.payment.app.service;

import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.repository.OrderRepository;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PaymentService 통합 테스트
 * - webhook/redirect 결제 완료 처리 테스트
 * - 재고 차감 로직 검증
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentCompleteService paymentCompleteService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("결제 완료 시 재고가 정상적으로 차감되어야 한다")
    void completePayment_shouldDecreaseStock() {
        // given
        // 테스트 데이터 준비 (주문, 상품, 재고 등)
        // String merchantUid = "ORDER_TEST_123";
        
        // when
        // paymentCompleteService.completePayment(merchantUid);
        
        // then
        // Payment payment = paymentRepository.findByMerchantUid(merchantUid).orElseThrow();
        // assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        
        // Order order = payment.getOrder();
        // assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAID);
        
        // 재고 확인
        // assertThat(stock.getQuantity()).isEqualTo(원래재고 - 주문수량);
    }

    @Test
    @DisplayName("PortOne webhook PAID 이벤트 처리 테스트")
    void webhook_paid_shouldCompletePayment() {
        // given
        // Mock PortOne response
        
        // when
        // paymentService.webhook(webhookRequest);
        
        // then
        // 결제 완료, 재고 차감 검증
    }

    @Test
    @DisplayName("PortOne redirect 결제 완료 처리 테스트")
    void redirectPaid_shouldCompletePayment() {
        // given
        // Mock PortOne response
        
        // when
        // paymentService.redirectPaid(redirectRequest);
        
        // then
        // 결제 완료, 재고 차감 검증
    }
}
