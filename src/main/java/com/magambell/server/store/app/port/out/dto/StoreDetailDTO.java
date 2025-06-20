package com.magambell.server.store.app.port.out.dto;

import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import java.time.LocalDateTime;
import java.util.List;

public record StoreDetailDTO(
        Long storeId,
        Long goodsId,
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
    public StoreDetailResponse toResponse() {
        return new StoreDetailResponse(storeId, goodsId, storeName, address, images, startTime, endTime, originalPrice,
                salePrice, discount, description, quantity);
    }
}
