package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.store.domain.entity.Store;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterGoodsServiceRequest(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        List<GoodsImagesRegister> goodsImageRegisters
) {

    public RegisterGoodsDTO toDTO(Store store) {
        return new RegisterGoodsDTO(startTime, endTime, quantity, originalPrice, discount, salePrice,
                store, goodsImageRegisters);
    }
}
