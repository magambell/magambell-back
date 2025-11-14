package com.magambell.server.store.app.port.out.response;

import com.magambell.server.goods.domain.enums.SaleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record OwnerStoreDetailDTO(
        Long storeId,
        String storeName,
        String address,
        Set<String> storeImageUrls,
        List<GoodsInfo> goodsList,
        List<GoodsImageInfo> goodsImageList
) {
    public record GoodsInfo(
            Long goodsId,
            String goodsName,
            Integer originPrice,
            Integer discount,
            Integer salePrice,
            String description,
            LocalDateTime startTime,
            LocalDateTime endTime,
            SaleStatus saleStatus,
            Integer stockQuantity
    ) {
        public String getGoodsId() {
            return String.valueOf(goodsId);
        }
    }

    public record GoodsImageInfo(
            Long goodsImageId,
            String goodsName,
            String imageUrl
    ) {
    }

    public String getStoreId() {
        return String.valueOf(storeId);
    }
}
