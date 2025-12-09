package com.magambell.server.goods.app.port.in.dto;

import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;

import java.time.LocalDateTime;
import java.util.List;

public record EditGoodsDTO(
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        List<GoodsImagesRegister> goodsImagesRegisters

) {

    public List<ImageRegister> toImage() {
        return goodsImagesRegisters.stream()
                .map(image -> new ImageRegister(image.id(), image.key()))
                .toList();
    }

}
