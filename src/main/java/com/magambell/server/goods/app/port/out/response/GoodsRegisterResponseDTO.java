package com.magambell.server.goods.app.port.out.response;

import java.util.List;

public record GoodsRegisterResponseDTO(
        List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages
) {
}
