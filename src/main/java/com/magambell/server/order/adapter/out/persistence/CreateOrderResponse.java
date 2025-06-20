package com.magambell.server.order.adapter.out.persistence;

public record CreateOrderResponse(
        String merchantUid,
        Integer totalAmount
) {
}
