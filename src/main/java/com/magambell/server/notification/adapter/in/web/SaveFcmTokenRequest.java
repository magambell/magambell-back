package com.magambell.server.notification.adapter.in.web;

import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record SaveFcmTokenRequest(
        @NotBlank(message = "토큰을 발급 받아 주세요.")
        String fcmToken
) {
    public SaveFcmTokenServiceRequest toService(final Long userId) {
        return new SaveFcmTokenServiceRequest(fcmToken, userId);
    }
}
