package com.magambell.server.order.app.port.out.response;

import com.magambell.server.order.domain.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderListDTO(
        Long orderId,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        Long storeId,
        String storeName,
        List<String> imageUrls,
        List<OrderGoodsInfo> goodsList,
        List<Long> reviewIds
) {
    public record OrderGoodsInfo(
            Long orderGoodsId,
            String goodsName,
            Integer quantity,
            Integer salePrice
    ) {
        public String getOrderGoodsId() {
            return String.valueOf(orderGoodsId);
        }
    }

    public String getOrderId() {
        return String.valueOf(orderId);
    }

    public String getStoreId() {
        return String.valueOf(storeId);
    }

    public List<String> getReviewIds() {
        return reviewIds.stream()
                .map(String::valueOf)
                .toList();
    }
}
