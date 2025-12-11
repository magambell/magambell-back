package com.magambell.server.goods.adapter.in.web;

public record GoodsImagesRegister(
        Integer id,
        String key,
        String imageUrl,
        String goodsName
) {
}
