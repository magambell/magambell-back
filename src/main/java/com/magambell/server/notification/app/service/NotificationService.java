package com.magambell.server.notification.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.out.NotificationCommandPort;
import com.magambell.server.notification.app.port.out.NotificationQueryPort;
import com.magambell.server.notification.domain.model.FcmToken;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService implements NotificationUseCase {

    private final NotificationCommandPort notificationCommandPort;
    private final NotificationQueryPort notificationQueryPort;
    private final StoreQueryPort storeQueryPort;
    private final UserQueryPort userQueryPort;

    @Transactional
    @Override
    public void saveToken(final SaveFcmTokenServiceRequest request) {
        User user = userQueryPort.findById(request.userId());
        Store store = storeQueryPort.findById(request.storeId());

        if (notificationQueryPort.existsByUserAndStore(user, store)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NOTIFICATION_STORE);
        }

        notificationCommandPort.save(FcmToken.create(request.fcmToken(), user, store));
    }
}
