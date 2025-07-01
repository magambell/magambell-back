package com.magambell.server.order.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.order.domain.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Adapter
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderRepository orderRepository;

    @Override
    public Order findById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public List<OrderListDTO> getOrderList(final Pageable pageable, final Long userId) {
        return orderRepository.getOrderList(pageable, userId);
    }

    @Override
    public OrderDetailDTO getOrderDetail(final Long orderId, final Long userId) {
        return orderRepository.getOrderDetail(orderId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public List<OrderStoreListDTO> getOrderStoreList(final Pageable pageable, final Long userId) {
        return orderRepository.getOrderStoreList(pageable, userId);
    }

    @Override
    public Order findWithAllById(final Long orderId) {
        return orderRepository.findWithAllById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }
}
