package com.magambell.server.order.app.port.out;

import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.domain.model.Order;
import java.util.List;

public interface OrderQueryPort {
    Order findById(Long orderId);

    List<OrderListDTO> getOrderList(Long userId);
}
