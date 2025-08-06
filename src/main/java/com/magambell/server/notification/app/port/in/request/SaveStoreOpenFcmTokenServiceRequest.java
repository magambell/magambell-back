package com.magambell.server.notification.app.port.in.request;

public record SaveStoreOpenFcmTokenServiceRequest(
        Long storeId,
        String fcmToken,
        Long userId
) {
}
