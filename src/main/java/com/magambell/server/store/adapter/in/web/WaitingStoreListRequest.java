package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.WaitingStoreListServiceRequest;
import jakarta.validation.constraints.Positive;

public record WaitingStoreListRequest(
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public WaitingStoreListServiceRequest toService() {
        return new WaitingStoreListServiceRequest(page, size);
    }
}
