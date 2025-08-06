package com.magambell.server.stock.app.port.out;

import com.magambell.server.stock.domain.model.Stock;

public interface StockQueryPort {
    Stock findByGoodsIdWithLock(Long goodsId);
}
