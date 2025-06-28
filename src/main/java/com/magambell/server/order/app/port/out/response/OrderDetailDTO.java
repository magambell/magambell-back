package com.magambell.server.order.app.port.out.response;

import com.magambell.server.order.adapter.out.persistence.OrderDetailResponse;
import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;

public record OrderDetailDTO(
        Long orderId,
        OrderStatus orderStatus,
        String storeName,
        String storeAddress,
        String imageUrl,
        Integer quantity,
        Integer totalPrice,
        LocalDateTime pickupTime,
        String memo
) {
    public OrderDetailResponse toResponse() {
        return new OrderDetailResponse(
                String.valueOf(orderId),
                orderStatus,
                storeName,
                storeAddress,
                imageUrl,
                quantity,
                totalPrice,
                pickupTime,
                memo
        );
    }
}
