package com.magambell.server.order.app.port.out.response;

import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderListDTO(
        Long orderId,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        Integer quantity,
        Integer salePrice,
        Long storeId,
        String storeName,
        List<String> imageUrls,
        String goodsName
) {
    public String getOrderId() {
        return String.valueOf(orderId);
    }

    public String getStoreId() {
        return String.valueOf(storeId);
    }
}
