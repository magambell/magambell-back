package com.magambell.server.notification.app.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.notification.app.port.in.request.DeleteStoreOpenFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.in.request.NotifyStoreOpenRequest;
import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.in.request.SaveStoreOpenFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.out.NotificationCommandPort;
import com.magambell.server.notification.app.port.out.NotificationQueryPort;
import com.magambell.server.notification.app.port.out.dto.FcmTokenDTO;
import com.magambell.server.notification.domain.model.FcmToken;
import com.magambell.server.notification.infra.FirebaseNotificationSender;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    public void saveStoreOpenToken(final SaveStoreOpenFcmTokenServiceRequest request) {
        User user = userQueryPort.findById(request.userId());
        Store store = storeQueryPort.findById(request.storeId());

        if (notificationQueryPort.existsByUserAndStore(user, store)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NOTIFICATION_STORE);
        }

        notificationCommandPort.save(FcmToken.create(request.fcmToken(), user, store));
    }

    @Transactional
    @Override
    public void deleteStoreOpenToken(final DeleteStoreOpenFcmTokenServiceRequest request) {
        FcmToken fcmToken = notificationQueryPort.findByUserIdAndStoreId(request.storeId(), request.userId());
        notificationCommandPort.delete(fcmToken);
    }

    @Transactional
    @Override
    public void saveToken(final SaveFcmTokenServiceRequest request) {
        User user = userQueryPort.findById(request.userId());

        notificationCommandPort.deleteUserAndStoreIsNull(user);
        notificationCommandPort.save(FcmToken.create(request.fcmToken(), user));
    }

    @Override
    public void notifyApproveOrder(final User user, final LocalDateTime pickupTime) {
        FcmTokenDTO token = notificationQueryPort.findWithAllByUserIdAndStoreIsNull(user);
        if (token != null) {
            String message = "주문이 수락됐어요. " + pickupTime.toLocalTime() + "에 마감백을 픽업 해주세요!";
            send(message, token);
        }
    }

    @Override
    public void notifyRejectOrder(final User user) {
        FcmTokenDTO token = notificationQueryPort.findWithAllByUserIdAndStoreIsNull(user);
        if (token != null) {
            String message = "주문이 거절됐어요.";
            send(message, token);
        }
    }

    @Override
    public void notifyPaidOrder(final Set<User> orderStoreOwnerList) {
        List<Long> ownerList = orderStoreOwnerList.stream()
                .map(User::getId).toList();

        List<FcmTokenDTO> tokens = notificationQueryPort.findWithAllByOwnerIdsAndStoreIsNull(ownerList);
        tokens.forEach(token -> send("새 주문이 들어왔어요!", token));
    }

    @Override
    public void testSendToken(final Long userId) {
        List<FcmToken> tokens = notificationQueryPort.findByUserId(userId);
        tokens.forEach(token -> send("FCM 테스트", new FcmTokenDTO(token.getId(), token.getToken(), "", "")));
    }


    @Override
    public void notifyStoreOpen(final NotifyStoreOpenRequest request) {
        List<FcmTokenDTO> tokens = notificationQueryPort.findWithAllByStoreId(request.store());

        tokens.forEach(token -> {
            String nickname = token.nickName();
            String storeName = token.storeName();
            String message = nickname + "님이 기다리던 " + storeName + "의 예약이 오픈되었어요!";

            send(message, token);
        });
    }

    private void send(final String message, final FcmTokenDTO token) {
        try {
            firebaseNotificationSender.send(token.token(), message, message);
        } catch (FirebaseMessagingException e) {
            fcmFail(e, token);
        }
    }

    private void fcmFail(final FirebaseMessagingException e, final FcmTokenDTO token) {
        log.warn("FCM 알림 전송 실패. tokenId={}, reason={}", token.fcmTokenId(), e.getMessage());
        notificationCommandPort.removeToken(token.fcmTokenId());
    }
}
