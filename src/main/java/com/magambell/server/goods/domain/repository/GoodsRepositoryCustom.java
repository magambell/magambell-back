package com.magambell.server.goods.domain.repository;

import com.magambell.server.goods.domain.entity.Goods;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GoodsRepositoryCustom {
    Optional<Goods> findWithStoreAndUserById(Long goodsId);

    Optional<Goods> findOwnedGoodsWithRelations(Long goodsId, Long userId);

    List<Goods> findExpiredGoods(LocalDateTime now);
}
