package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;

import java.util.List;

public record EditGoodsImagesServiceRequest(
        Long goodsId,
        Long userId,
        List<GoodsImagesRegister> goodsImagesRegisters
) {
}
