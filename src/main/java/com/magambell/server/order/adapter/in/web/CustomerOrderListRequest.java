package com.magambell.server.order.adapter.in.web;

import com.magambell.server.order.app.port.in.request.CustomerOrderListServiceRequest;
import jakarta.validation.constraints.Positive;

public record CustomerOrderListRequest(
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public CustomerOrderListServiceRequest toService() {
        return new CustomerOrderListServiceRequest(page, size);
    }
}
