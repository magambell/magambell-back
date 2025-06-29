package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.store.domain.model.Store;
import java.time.LocalDateTime;

public record RegisterGoodsServiceRequest(
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice
) {

    public RegisterGoodsDTO toDTO(Store store) {
        return new RegisterGoodsDTO(startTime, endTime, quantity, originalPrice, discount, salePrice, description,
                store);
    }
}
