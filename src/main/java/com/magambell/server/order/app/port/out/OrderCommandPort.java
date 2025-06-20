package com.magambell.server.order.app.port.out;

import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.domain.model.Order;

public interface OrderCommandPort {
    Order createOrder(CreateOrderDTO dto);
}
