package com.magambell.server.payment.app.port.in.dto;

import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.enums.PaymentStatus;

public record CreatePaymentDTO(
        Order order,
        Integer amount,
        PaymentStatus paymentStatus
) {

    public Payment toPayment() {
        return Payment.create(this);
    }
}
