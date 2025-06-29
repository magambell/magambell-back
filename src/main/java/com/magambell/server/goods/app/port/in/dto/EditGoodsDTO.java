package com.magambell.server.goods.app.port.in.dto;

import java.time.LocalDateTime;

public record EditGoodsDTO(
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice
) {
}
