package com.magambell.server.goods.app.port.in.dto;

import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.store.domain.model.Store;
import java.time.LocalTime;

public record RegisterGoodsDTO(
        String name,
        LocalTime startTime,
        LocalTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        String description,
        Store store
) {

    public Goods toGoods() {
        return Goods.create(this);
    }

    public Stock toStock() {
        return Stock.create(quantity);
    }
}
