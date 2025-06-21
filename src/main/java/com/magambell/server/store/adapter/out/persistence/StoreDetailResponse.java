package com.magambell.server.store.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

public record StoreDetailResponse(
        String storeId,
        String goodsId,
        String storeName,
        String address,
        List<String> images,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer originalPrice,
        Integer salePrice,
        Integer discount,
        String description,
        Integer quantity
) {
}
