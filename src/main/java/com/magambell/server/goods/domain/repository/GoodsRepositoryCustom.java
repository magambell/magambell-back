package com.magambell.server.goods.domain.repository;

import com.magambell.server.goods.domain.model.Goods;
import java.util.Optional;

public interface GoodsRepositoryCustom {
    Optional<Goods> findWithStoreAndUserById(Long goodsId);
}
