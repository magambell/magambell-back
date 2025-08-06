package com.magambell.server.goods.app.port.out;

import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;

public interface GoodsCommandPort {
    void registerGoods(RegisterGoodsDTO dto);
}
