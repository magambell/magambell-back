package com.magambell.server.notification.infra;

import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirebaseNotificationSender {

    private static final String UNIFIED_NOTIFICATION_TITLE = "바이트픽";

    public void send(String fcmToken, String title, String body) throws FirebaseMessagingException {
        log.info("FCM 알림 전송 시작 - token: {}, title: {}, body: {}", fcmToken, UNIFIED_NOTIFICATION_TITLE, body);
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(UNIFIED_NOTIFICATION_TITLE)
                        .setBody(body)
                        .build())
            .setApnsConfig(ApnsConfig.builder()
                .setAps(Aps.builder()
                    .setSound("default")
                    .build())
                .build())
                .build();

        String messageId = FirebaseMessaging.getInstance().send(message);
        log.info("FCM 알림 전송 성공 - token: {}, messageId: {}", fcmToken, messageId);
    }
}
