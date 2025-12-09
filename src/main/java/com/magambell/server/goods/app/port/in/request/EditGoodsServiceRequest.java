package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.in.dto.EditGoodsDTO;

import java.time.LocalDateTime;
import java.util.List;

public record EditGoodsServiceRequest(
        Long goodsId,
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        Long userId,
        List<GoodsImagesRegister> goodsImagesRegisters

) {
    public EditGoodsDTO toDTO() {
        return new EditGoodsDTO(name, startTime, endTime, quantity, originalPrice, discount, salePrice, goodsImagesRegisters);
    }
}
