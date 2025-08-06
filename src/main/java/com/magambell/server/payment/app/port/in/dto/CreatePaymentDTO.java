package com.magambell.server.payment.app.port.in.dto;

import com.magambell.server.order.domain.model.Order;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import com.magambell.server.payment.domain.model.Payment;

public record CreatePaymentDTO(
        Order order,
        Integer amount,
        PaymentStatus paymentStatus
) {

    public Payment toPayment() {
        return Payment.create(this);
    }
}
