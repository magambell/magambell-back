package com.magambell.server.goods.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.app.port.out.GoodsCommandPort;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.store.domain.model.Store;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class GoodsCommandAdapter implements GoodsCommandPort {

    private final GoodsRepository goodsRepository;

    @Override
    public void registerGoods(final RegisterGoodsDTO dto) {
        Store store = dto.store();
        Goods goods = dto.toGoods();

        store.addGoods(goods);
        goodsRepository.save(goods);
    }
}
