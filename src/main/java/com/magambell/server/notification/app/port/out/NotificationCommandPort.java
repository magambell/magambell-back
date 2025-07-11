package com.magambell.server.notification.app.port.out;

import com.magambell.server.notification.domain.model.FcmToken;

public interface NotificationCommandPort {
    void save(FcmToken fcmToken);

    void removeToken(Long fcmTokenId);
}
