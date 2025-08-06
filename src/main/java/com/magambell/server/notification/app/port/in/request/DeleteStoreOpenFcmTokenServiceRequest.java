package com.magambell.server.notification.app.port.in.request;

public record DeleteStoreOpenFcmTokenServiceRequest(
        Long storeId,
        Long userId
) {
}
