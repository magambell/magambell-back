package com.magambell.server.goods.adapter.in.web;

import com.magambell.server.goods.app.port.in.request.EditGoodsImagesServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EditGoodsImagesRequest(

        @NotNull(message = "상품을 선택해 주세요.")
        Long goodsId,

        @NotEmpty(message = "상품 이미지는 필수입니다.")
        List<GoodsImagesRegister> goodsImagesRegisters
) {
    public EditGoodsImagesServiceRequest toService(final Long userId) {
        return new EditGoodsImagesServiceRequest(goodsId, userId, goodsImagesRegisters);
    }
}
