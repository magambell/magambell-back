package com.magambell.server.stock.domain.repository;

import com.magambell.server.stock.domain.model.Stock;
import java.util.Optional;

public interface StockRepositoryCustom {
    Optional<Stock> findByGoodsIdWithLock(Long goodsId);
}
