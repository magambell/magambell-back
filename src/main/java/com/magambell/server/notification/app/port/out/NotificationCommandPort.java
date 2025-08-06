package com.magambell.server.notification.app.port.out;

import com.magambell.server.notification.domain.model.FcmToken;
import com.magambell.server.user.domain.model.User;

public interface NotificationCommandPort {
    void save(FcmToken fcmToken);

    void removeToken(Long fcmTokenId);

    void deleteUserAndStoreIsNull(User user);

    void delete(FcmToken fcmToken);
}
