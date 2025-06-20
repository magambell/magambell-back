package com.magambell.server.goods.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class GoodsQueryAdapter implements GoodsQueryPort {
    private final GoodsRepository goodsRepository;

    @Override
    public Goods findById(final Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GOODS_NOT_FOUND));
    }
}
