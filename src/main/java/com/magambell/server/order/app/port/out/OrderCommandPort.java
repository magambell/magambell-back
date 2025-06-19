package com.magambell.server.order.app.port.out;

import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;

public interface OrderCommandPort {
    void createOrder(CreateOrderDTO dto);
}
