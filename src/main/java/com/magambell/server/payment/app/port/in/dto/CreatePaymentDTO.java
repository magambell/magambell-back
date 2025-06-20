package com.magambell.server.payment.app.port.in.dto;

import com.magambell.server.order.domain.model.Order;
import com.magambell.server.payment.domain.enums.EasyPayProvider;
import com.magambell.server.payment.domain.enums.PayType;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import com.magambell.server.payment.domain.model.Payment;

public record CreatePaymentDTO(
        Order order,
        Integer amount,
        PayType payType,
        String cardName,
        EasyPayProvider easyPayProvider,
        PaymentStatus paymentStatus
) {

    public Payment toPayment() {
        return Payment.create(this);
    }
}
