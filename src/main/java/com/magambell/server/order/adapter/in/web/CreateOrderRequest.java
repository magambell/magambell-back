package com.magambell.server.order.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record CreateOrderRequest(
        @NotNull(message = "상품을 선택해 주세요.")
        Long goodsId,

        @Positive(message = "주문 개수는 1개 이상 이어야 합니다.")
        Integer quantity,

        @PositiveOrZero(message = "주문 금액은 0원 이상 이어야 합니다.")
        Integer totalPrice,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX][X]", timezone = "Asia/Seoul")
        @NotNull(message = "픽업시간을 설정해 주세요.")
        LocalDateTime pickupTime,
        String memo
) {


    public CreateOrderServiceRequest toServiceRequest() {
        // UTC로 들어온 경우 KST로 변환
        LocalDateTime kstPickupTime = pickupTime;
        return new CreateOrderServiceRequest(goodsId, quantity, totalPrice, kstPickupTime, memo);
    }
}
