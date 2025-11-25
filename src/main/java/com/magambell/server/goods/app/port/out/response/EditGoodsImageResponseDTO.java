package com.magambell.server.goods.app.port.out.response;


import java.util.List;

public record EditGoodsImageResponseDTO(Long id, List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages) {
}
