package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import jakarta.validation.constraints.NotNull;

public record CloseStoreListRequest(
        @NotNull(message = "위도를 입력해 주세요.")
        Double latitude,

        @NotNull(message = "경도를 입력해 주세요.")
        Double longitude
) {
    public CloseStoreListServiceRequest toService() {
        return new CloseStoreListServiceRequest(latitude, longitude);
    }
}
