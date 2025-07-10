package com.magambell.server.order.app.port.out.response;

import com.magambell.server.order.adapter.out.persistence.OrderDetailResponse;
import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailDTO(
        Long orderId,
        List<Long> orderGoodsIds,
        OrderStatus orderStatus,
        String storeName,
        String storeAddress,
        String imageUrl,
        Integer quantity,
        Integer totalPrice,
        LocalDateTime pickupTime,
        String memo,
        LocalDateTime createdAt,
        Long storeId,
        Long reviewId
) {
    public OrderDetailResponse toResponse() {

        return new OrderDetailResponse(
                String.valueOf(orderId),
                orderGoodsIds.stream()
                        .map(String::valueOf)
                        .toList(),
                orderStatus,
                storeName,
                storeAddress,
                imageUrl,
                quantity,
                totalPrice,
                pickupTime,
                memo,
                createdAt,
                String.valueOf(storeId),
                String.valueOf(reviewId)
        );
    }
}
