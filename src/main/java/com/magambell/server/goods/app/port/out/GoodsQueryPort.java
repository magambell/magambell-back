package com.magambell.server.goods.app.port.out;

import com.magambell.server.goods.domain.model.Goods;

public interface GoodsQueryPort {
    Goods findWithStoreAndUserById(Long goodsId);

    boolean existsByStoreId(Long storeId);

    Goods findById(Long goodsId);

    Goods findOwnedGoodsWithRelations(Long goodsId, Long userId);
}
