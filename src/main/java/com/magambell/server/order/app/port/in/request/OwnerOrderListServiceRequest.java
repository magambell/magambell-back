package com.magambell.server.order.app.port.in.request;

import com.magambell.server.order.domain.enums.OrderStatus;

public record OwnerOrderListServiceRequest(
        Integer page,
        Integer size,
        OrderStatus orderStatus
) {
}
