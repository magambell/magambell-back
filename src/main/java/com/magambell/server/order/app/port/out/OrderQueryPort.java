package com.magambell.server.order.app.port.out;

import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.model.Order;
import java.util.List;

public interface OrderQueryPort {
    Order findById(Long orderId);

    List<OrderListDTO> getOrderList(Long userId);

    OrderDetailDTO getOrderDetail(Long orderId, Long userId);

    List<OrderStoreListDTO> getOrderStoreList(Long userId);

    Order findWithAllById(Long orderId);
}
