package com.magambell.server.store.app.port.out.response;

import java.time.LocalTime;
import java.util.List;

public record StoreListDTOResponse(
        Long storeId,
        String storeName,
        List<String> ImageUrl,
        String goodsName,
        LocalTime startTime,
        LocalTime endTime,
        Integer originPrice,
        Integer discount,
        Integer salePrice,
        Integer quantity,
        Double distance
) {
}
