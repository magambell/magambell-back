package com.magambell.server.goods.app.port.out;

import com.magambell.server.goods.domain.model.Goods;

public interface GoodsQueryPort {
    Goods findById(Long goodsId);
}
