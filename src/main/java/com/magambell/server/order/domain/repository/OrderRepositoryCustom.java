package com.magambell.server.order.domain.repository;

import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.order.domain.model.OrderGoods;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    List<OrderListDTO> getOrderList(Pageable pageable, Long userId);

    Optional<OrderDetailDTO> getOrderDetail(Long orderId, Long userId);

    List<OrderStoreListDTO> getOrderStoreList(Pageable pageable, Long userId, OrderStatus orderStatus);

    Optional<Order> findOwnerWithAllById(Long orderId);

    Optional<Order> findWithAllById(Long orderId);

    Optional<OrderGoods> findOrderGoodsWithOrderById(Long orderGoodsId);

    List<Order> findOrdersToNotifyByPickupTime(LocalDateTime pickupTime);
}
