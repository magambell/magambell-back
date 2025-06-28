package com.magambell.server.order.domain.repository;

import com.magambell.server.order.app.port.out.response.OrderListDTO;
import java.util.List;

public interface OrderRepositoryCustom {
    List<OrderListDTO> getOrderList(Long userId);
}
