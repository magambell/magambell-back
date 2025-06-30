package com.magambell.server.order.app.port.in;

import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;
import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import java.util.List;

public interface OrderUseCase {
    CreateOrderResponseDTO createOrder(CreateOrderServiceRequest serviceRequest, Long userId);

    List<OrderListDTO> getOrderList(Long userId);

    OrderDetailDTO getOrderDetail(Long orderId, Long userId);

    List<OrderStoreListDTO> getOrderStoreList(Long userId);
}
