package com.magambell.server.store.app.port.out.response;

import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.store.domain.enums.Bank;
import java.time.LocalDateTime;
import java.util.Set;

public record StoreAdminListDTO(
        Long storeId,
        String storeName,
        Set<String> ImageUrl,
        Double latitude,
        Double longitude,
        String address,
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
        String bankAccount
) {
    public String getStoreId() {
        return String.valueOf(storeId);
    }
}
