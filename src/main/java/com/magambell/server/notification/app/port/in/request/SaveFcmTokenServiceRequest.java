package com.magambell.server.notification.app.port.in.request;

public record SaveFcmTokenServiceRequest(
        Long storeId,
        String fcmToken,
        Long userId
) {
}
