package com.magambell.server.goods.adapter.in.web;

import com.magambell.server.goods.app.port.in.request.RegisterGoodsServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalTime;

public record RegisterGoodsRequest(
        @NotBlank(message = "상품 이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "상품 설명을 입력해 주세요.")
        String description,

        @NotNull(message = "판매 시작 시간은 선택해 주세요.")
        LocalTime startTime,

        @NotNull(message = "판매 마감 시간은 선택해 주세요.")
        LocalTime endTime,

        @Positive(message = "판매 개수는 1개 이상 이어야 합니다.")
        Integer quantity,

        @PositiveOrZero(message = "정가는 0원 이상 이어야 합니다.")
        Integer originalPrice,

        @PositiveOrZero(message = "할인율은 0% 이상 이어야 합니다.")
        Integer discount,

        @PositiveOrZero(message = "판매가는 0원 이상 이어야 합니다.")
        Integer salePrice

) {
    public RegisterGoodsServiceRequest toService() {
        return new RegisterGoodsServiceRequest(
                name, description, startTime, endTime, quantity, originalPrice, discount, salePrice
        );
    }
}
