package com.magambell.server.goods.adapter.out.persistence;

import com.magambell.server.goods.app.port.out.response.GoodsPreSignedUrlImage;

import java.util.List;

public record GoodsImagesResponse(
        List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages
) {
}
