package com.magambell.server.notification.adapter.in.web;

import com.magambell.server.notification.app.port.in.request.SaveStoreOpenFcmTokenServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveStoreOpenFcmTokenRequest(
        @NotNull(message = "매장을 선택해 주세요.")
        Long storeId,
        @NotBlank(message = "토큰을 발급 받아 주세요.")
        String fcmToken
) {
    public SaveStoreOpenFcmTokenServiceRequest toService(final Long userId) {
        return new SaveStoreOpenFcmTokenServiceRequest(storeId, fcmToken, userId);
    }
}
