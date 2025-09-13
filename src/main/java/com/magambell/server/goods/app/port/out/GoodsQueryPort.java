package com.magambell.server.goods.app.port.out;

import com.magambell.server.goods.domain.entity.Goods;
import java.time.LocalDateTime;
import java.util.List;

public interface GoodsQueryPort {
    Goods findWithStoreAndUserById(Long goodsId);

    boolean existsByStoreId(Long storeId);

    Goods findById(Long goodsId);

    Goods findOwnedGoodsWithRelations(Long goodsId, Long userId);

    List<Goods> findExpiredGoods(LocalDateTime now);
}
