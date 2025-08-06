package com.magambell.server.order.adapter.out.persistence;

import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;

public record OrderDetailResponse(
        String orderId,
        String orderGoodsId,
        OrderStatus orderStatus,
        String storeName,
        String storeAddress,
        String imageUrl,
        Integer quantity,
        Integer totalPrice,
        LocalDateTime pickupTime,
        String memo,
        LocalDateTime createdAt,
        String storeId,
        String reviewId
) {

}
