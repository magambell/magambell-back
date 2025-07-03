package com.magambell.server.store.app.port.out.response;

import com.magambell.server.goods.domain.enums.SaleStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OwnerStoreDetailDTO(
        Long storeId,
        String storeName,
        List<String> storeImageUrls,
        List<GoodsInfo> goodsList
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

    public String getStoreId() {
        return String.valueOf(storeId);
    }
}
