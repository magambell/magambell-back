package com.magambell.server.order.app.port.in;

import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import java.util.List;

public interface OrderUseCase {
    CreateOrderResponseDTO createOrder(CreateOrderServiceRequest serviceRequest, Long userId);

    List<OrderListDTO> getOrderList(Long userId);
}
