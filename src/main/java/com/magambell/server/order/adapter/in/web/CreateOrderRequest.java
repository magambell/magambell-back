package com.magambell.server.order.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public record CreateOrderRequest(
        @NotNull(message = "상품을 선택해 주세요.")
        Long goodsId,

        @Positive(message = "주문 개수는 1개 이상 이어야 합니다.")
        Integer quantity,

        @PositiveOrZero(message = "주문 금액은 0원 이상 이어야 합니다.")
        Integer totalPrice,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX][X]")
        @NotNull(message = "픽업시간을 설정해 주세요.")
        LocalDateTime pickupTime,

        @Schema(description = "주문 메모 (선택사항)", nullable = true, example = "빨리 주세요")
        String memo
) {


    public CreateOrderServiceRequest toServiceRequest() {
        return new CreateOrderServiceRequest(goodsId, quantity, totalPrice, pickupTime, memo);
    }
}
