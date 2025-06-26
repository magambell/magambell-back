package com.magambell.server.order.app.port.out;

import com.magambell.server.order.domain.model.Order;

public interface OrderQueryPort {
    Order findById(Long orderId);
}
