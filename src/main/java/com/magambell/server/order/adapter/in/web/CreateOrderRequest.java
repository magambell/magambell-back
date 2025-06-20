package com.magambell.server.order.adapter.in.web;

import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.payment.domain.enums.EasyPayProvider;
import com.magambell.server.payment.domain.enums.PayType;
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

        @NotNull(message = "픽업시간을 설정해 주세요.")
        LocalDateTime pickupTime,
        String memo,

        @NotNull(message = "결제를 선택해 주세요.")
        PayType payType,

        String cardName,

        EasyPayProvider easyPayProvider
) {


    public CreateOrderServiceRequest toServiceRequest() {
        return new CreateOrderServiceRequest(goodsId, quantity, totalPrice, pickupTime, memo, payType, cardName,
                easyPayProvider);
    }
}
