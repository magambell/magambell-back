package com.magambell.server.order.adapter.in.web;

import com.magambell.server.order.app.port.in.request.OwnerOrderListServiceRequest;
import com.magambell.server.order.domain.enums.OrderStatus;
import jakarta.validation.constraints.Positive;

public record OwnerOrderListRequest(
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size,
        OrderStatus orderStatus
) {
    public OwnerOrderListServiceRequest toService() {
        return new OwnerOrderListServiceRequest(page, size, orderStatus);
    }
}
