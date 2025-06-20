package com.magambell.server.order.app.port.in;

import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;

public interface OrderUseCase {
    CreateOrderResponseDTO createOrder(CreateOrderServiceRequest serviceRequest, Long userId);
}
