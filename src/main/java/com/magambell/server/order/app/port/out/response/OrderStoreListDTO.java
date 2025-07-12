package com.magambell.server.order.app.port.out.response;

import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;

public record OrderStoreListDTO(
        Long orderId,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        LocalDateTime pickupTime,
        String memo,
        Integer quantity,
        Integer totalPrice,
        String phoneNumber,
        String goodsName
) {
    public String getOrderId() {
        return String.valueOf(orderId);
    }

}
