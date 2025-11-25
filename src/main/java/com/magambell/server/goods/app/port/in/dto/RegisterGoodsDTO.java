package com.magambell.server.goods.app.port.in.dto;

import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.stock.domain.entity.StockHistory;
import com.magambell.server.stock.domain.enums.StockType;
import com.magambell.server.store.domain.entity.Store;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterGoodsDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        String description,
        Store store,
        List<GoodsImagesRegister> goodsImagesRegisters

) {

    public Goods toGoods() {
        return Goods.create(this);
    }

    public StockHistory toStock() {
        return StockHistory.create(StockType.INIT, 0, quantity, quantity);
    }

    public List<ImageRegister> toImage() {
        return goodsImagesRegisters.stream()
                .map(image -> new ImageRegister(image.id(), image.key()))
                .toList();
    }
}
