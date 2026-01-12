package com.magambell.server.notification.infra;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirebaseNotificationSender {

    public void send(String fcmToken, String title, String body) throws FirebaseMessagingException {
        log.info("FCM 알림 전송 시작 - token: {}, title: {}, body: {}", fcmToken, title, body);
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        String messageId = FirebaseMessaging.getInstance().send(message);
        log.info("FCM 알림 전송 성공 - token: {}, messageId: {}", fcmToken, messageId);
    }
}
