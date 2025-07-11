package com.magambell.server.notification.app.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.notification.app.port.in.request.NotifyStoreOpenRequest;
import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.out.NotificationCommandPort;
import com.magambell.server.notification.app.port.out.NotificationQueryPort;
import com.magambell.server.notification.app.port.out.dto.FcmTokenDTO;
import com.magambell.server.notification.domain.model.FcmToken;
import com.magambell.server.notification.infra.FirebaseNotificationSender;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService implements NotificationUseCase {

    private final NotificationCommandPort notificationCommandPort;
    private final NotificationQueryPort notificationQueryPort;
    private final FirebaseNotificationSender firebaseNotificationSender;
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

    @Override
    public void notifyStoreOpen(final NotifyStoreOpenRequest request) {
        List<FcmTokenDTO> tokens = notificationQueryPort.findWithAllByStoreId(request.store().getId());

        tokens.forEach(token -> {
            try {
                String nickname = token.nickName();
                String storeName = token.storeName();

                String message = nickname + "님이 기다리던 " + storeName + "의 예약이 오픈되었어요!";

                firebaseNotificationSender.send(token.token(), message, message);
            } catch (FirebaseMessagingException e) {
                log.warn("FCM 알림 전송 실패. tokenId={}, reason={}", token.fcmTokenId(), e.getMessage());
                notificationCommandPort.removeToken(token.fcmTokenId());
            }
        });
    }
}
