package com.magambell.server.notification.adapter.in.web;

import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveFcmTokenRequest(
        @NotNull(message = "매장을 선택해 주세요.")
        Long storeId,
        @NotBlank(message = "토큰을 발급 받아 주세요.")
        String fcmToken
) {
    public SaveFcmTokenServiceRequest toService(final Long userId) {
        return new SaveFcmTokenServiceRequest(storeId, fcmToken, userId);
    }
}
