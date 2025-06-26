package com.magambell.server.order.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderRepository orderRepository;

    @Override
    public Order findById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }
}
