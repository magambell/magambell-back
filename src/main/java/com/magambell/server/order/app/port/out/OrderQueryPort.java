package com.magambell.server.order.app.port.out;

import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.entity.OrderGoods;
import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderQueryPort {
    Order findById(Long orderId);

    List<OrderListDTO> getOrderList(Pageable pageable, Long userId);

    OrderDetailDTO getOrderDetail(Long orderId, Long userId);

    List<OrderStoreListDTO> getOrderStoreList(Pageable pageable, Long userId, OrderStatus orderStatus);

    Order findOwnerWithAllById(Long orderId);

    Order findWithAllById(Long orderId);

    OrderGoods findOrderGoodsById(Long orderGoodsId);

    List<Order> findOrdersToNotifyByPickupTime(LocalDateTime pickupTime);

    List<Order> findByPaidBeforePickupRejectProcessedOrders(LocalDateTime pickupTime, LocalDateTime createdAtCutOff);

    List<Order> findByAutoRejectProcessedOrders(LocalDateTime minusMinutes, LocalDateTime pickupTime);
}
