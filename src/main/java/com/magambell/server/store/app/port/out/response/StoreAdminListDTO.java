package com.magambell.server.store.app.port.out.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.store.domain.enums.Bank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record StoreAdminListDTO(
        Long storeId,
        String storeName,
    @JsonProperty("storeImages") Set<String> ImageUrl,
        Double latitude,
        Double longitude,
        String address,
    String description,
    String parkingDescription,
        String goodsName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer originPrice,
        Integer discount,
        Integer salePrice,
        Integer quantity,
        Double distance,
        SaleStatus saleStatus,
        String nickName,
        String ownerName,
        String ownerPhone,
        String businessNumber,
        Bank bankName,
        String bankAccount,
        List<GoodsInfo> goodsList
) {
    public record GoodsInfo(
            Long goodsId,
            String goodsName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer originPrice,
            Integer discount,
            Integer salePrice,
            Integer quantity,
            SaleStatus saleStatus
    ) {
        public String getGoodsId() {
            return String.valueOf(goodsId);
        }
    }

    public String getStoreId() {
        return String.valueOf(storeId);
    }
}
