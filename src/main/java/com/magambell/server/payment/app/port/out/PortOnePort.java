package com.magambell.server.payment.app.port.out;

import com.magambell.server.payment.infra.PortOnePaymentResponse;

public interface PortOnePort {
    PortOnePaymentResponse getPaymentById(String paymentId);

    void cancelPayment(String paymentId, Integer totalPrice, String reason);
}
