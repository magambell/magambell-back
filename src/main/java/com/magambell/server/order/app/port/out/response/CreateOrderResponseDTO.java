package com.magambell.server.order.app.port.out.response;

public record CreateOrderResponseDTO(
        String merchantUid,
        Integer totalAmount
) {
}
