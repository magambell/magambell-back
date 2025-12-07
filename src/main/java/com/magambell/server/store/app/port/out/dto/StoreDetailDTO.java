package com.magambell.server.store.app.port.out.dto;

import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record StoreDetailDTO(
        Long storeId,
        Long goodsId,
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
        String parkingDescription,
        List<GoodsImagesRegister> goodsImages
) {
    public StoreDetailResponse toResponse(Long reviewCount,
                                          Double averageRating) {
        Double roundedRating = averageRating != null
                ? Math.round(averageRating * 10.0) / 10.0
                : 0.0;

        return new StoreDetailResponse(String.valueOf(storeId), String.valueOf(goodsId), storeName, address, images,
                startTime, endTime, originalPrice,
                salePrice, discount, description, quantity, saleStatus, latitude, longitude, parkingDescription, reviewCount, roundedRating, goodsImages);
    }
}
