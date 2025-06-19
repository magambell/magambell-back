package com.magambell.server.order.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.app.port.out.OrderCommandPort;
import com.magambell.server.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class OrderCommandAdapter implements OrderCommandPort {

    private final OrderRepository orderRepository;

    @Override
    public void createOrder(final CreateOrderDTO dto) {
        orderRepository.save(dto.toOrder());
    }
}
