package com.magambell.server.order.app.port.in;

import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;

public interface OrderUseCase {
    void createOrder(CreateOrderServiceRequest serviceRequest, Long userId);
}
