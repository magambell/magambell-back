package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.RegisterOpenRegionServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record OpenRegionRequest(
        @NotBlank(message = "지역을 입력해 주세요.")
        String region
) {
    public RegisterOpenRegionServiceRequest toServiceRequest() {
        return new RegisterOpenRegionServiceRequest(
               region
        );
    }
}
