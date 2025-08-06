package com.magambell.server.goods.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class GoodsQueryAdapter implements GoodsQueryPort {
    private final GoodsRepository goodsRepository;

    @Override
    public Goods findWithStoreAndUserById(final Long goodsId) {
        return goodsRepository.findWithStoreAndUserById(goodsId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GOODS_NOT_FOUND));
    }

    @Override
    public boolean existsByStoreId(final Long storeId) {
        return goodsRepository.existsByStoreId(storeId);
    }

    @Override
    public Goods findById(final Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GOODS_NOT_FOUND));
    }

    @Override
    public Goods findOwnedGoodsWithRelations(final Long goodsId, final Long userId) {
        return goodsRepository.findOwnedGoodsWithRelations(goodsId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GOODS_NOT_FOUND));
    }

    @Override
    public List<Goods> findExpiredGoods(final LocalDateTime now) {
        return goodsRepository.findExpiredGoods(now);
    }
}
