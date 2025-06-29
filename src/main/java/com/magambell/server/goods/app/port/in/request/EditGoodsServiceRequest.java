package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.goods.app.port.in.dto.EditGoodsDTO;
import java.time.LocalDateTime;

public record EditGoodsServiceRequest(
        Long goodsId,
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        Long userId
) {
    public EditGoodsDTO toDTO() {
        return new EditGoodsDTO(name, description, startTime, endTime, quantity, originalPrice, discount, salePrice);
    }
}
