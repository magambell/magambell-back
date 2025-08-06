package com.magambell.server.notification.app.port.in.request;

public record SaveFcmTokenServiceRequest(
        String fcmToken,
        Long userId
) {
}
