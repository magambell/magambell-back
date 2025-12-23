package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.RegisterOpenRegionServiceRequest;
import jakarta.validation.constraints.NotNull;

public record OpenRegionRequest(
        @NotNull(message = "지역 ID를 입력해 주세요.")
        Long regionId
) {
    public RegisterOpenRegionServiceRequest toServiceRequest() {
        return new RegisterOpenRegionServiceRequest(
               regionId
        );
    }
}
