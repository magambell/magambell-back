package com.magambell.server.goods.adapter.in.web;

import com.magambell.server.goods.app.port.in.request.EditGoodsServiceRequest;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record EditGoodsRequest(

        @NotNull(message = "상품을 선택해 주세요.")
        Long goodsId,

        @NotBlank(message = "상품 이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "상품 설명을 입력해 주세요.")
        String description,

        @NotNull(message = "판매 시작 시간은 선택해 주세요.")
        LocalDateTime startTime,

        @NotNull(message = "판매 마감 시간은 선택해 주세요.")
        LocalDateTime endTime,

        @Positive(message = "판매 개수는 1개 이상 이어야 합니다.")
        Integer quantity,

        @PositiveOrZero(message = "정가는 0원 이상 이어야 합니다.")
        Integer originalPrice,

        @PositiveOrZero(message = "할인율은 0% 이상 이어야 합니다.")
        Integer discount,

        @PositiveOrZero(message = "판매가는 0원 이상 이어야 합니다.")
        Integer salePrice,

        @NotEmpty(message = "상품 이미지는 필수입니다.")
        List<GoodsImagesRegister> goodsImagesRegisters
) {
    public EditGoodsServiceRequest toService(final Long userId) {
        return new EditGoodsServiceRequest(goodsId, name, description, startTime, endTime, quantity, originalPrice,
                discount,
                salePrice, userId, goodsImagesRegisters);
    }
}
