package com.magambell.server.store.app.port.out.response;

import com.magambell.server.goods.domain.enums.SaleStatus;
import java.time.LocalDateTime;
import java.util.List;

public record StoreListDTOResponse(
        Long storeId,
        String storeName,
        List<String> ImageUrl,
        String goodsName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer originPrice,
        Integer discount,
        Integer salePrice,
        Integer quantity,
        Double distance,
        SaleStatus saleStatus
) {
}
