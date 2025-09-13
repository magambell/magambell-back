package com.magambell.server.goods.app.port.in.dto;

import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.stock.domain.enums.StockType;
import com.magambell.server.stock.domain.entity.StockHistory;
import com.magambell.server.store.domain.entity.Store;
import java.time.LocalDateTime;

public record RegisterGoodsDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
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

    public StockHistory toStock() {
        return StockHistory.create(StockType.INIT, 0, quantity, quantity);
    }
}
