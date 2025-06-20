package com.magambell.server.payment.app.port.in;

import com.magambell.server.order.domain.model.Order;

public interface PaymentUseCase {
    void createCheckout(Order order);
}
