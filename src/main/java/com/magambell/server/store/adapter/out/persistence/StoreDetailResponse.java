package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.goods.domain.enums.SaleStatus;
import java.time.LocalDateTime;
import java.util.Set;

public record StoreDetailResponse(
        String storeId,
        String goodsId,
        String storeName,
        String address,
        Set<String> images,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer originalPrice,
        Integer salePrice,
        Integer discount,
        String description,
        Integer quantity,
        SaleStatus saleStatus,
        Double latitude,
        Double longitude,
        Long reviewCount,
        Double averageRating

) {
}
