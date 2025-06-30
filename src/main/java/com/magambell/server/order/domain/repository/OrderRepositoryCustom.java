package com.magambell.server.order.domain.repository;

import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {
    List<OrderListDTO> getOrderList(Long userId);

    Optional<OrderDetailDTO> getOrderDetail(Long orderId, Long userId);

    List<OrderStoreListDTO> getOrderStoreList(Long userId);

    Optional<Order> findWithAllById(Long orderId);
}
