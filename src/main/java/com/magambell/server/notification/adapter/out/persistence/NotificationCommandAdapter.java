package com.magambell.server.notification.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.notification.app.port.out.NotificationCommandPort;
import com.magambell.server.notification.domain.model.FcmToken;
import com.magambell.server.notification.domain.repository.FcmTokenRepository;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class NotificationCommandAdapter implements NotificationCommandPort {

    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void save(final FcmToken fcmToken) {
        fcmTokenRepository.save(fcmToken);
    }

    @Override
    public void removeToken(final Long fcmTokenId) {
        fcmTokenRepository.deleteById(fcmTokenId);
    }

    @Override
    public void deleteUserAndStoreIsNull(final User user) {
        fcmTokenRepository.deleteByUserIdAndStoreIsNull(user.getId());
    }

    @Override
    public void delete(final FcmToken fcmToken) {
        fcmTokenRepository.delete(fcmToken);
    }
}
