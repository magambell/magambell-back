package com.magambell.server.notification.app.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.notification.adapter.in.web.CheckStoreOpenServiceRequest;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.notification.app.port.in.request.DeleteStoreOpenFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.in.request.NotifyStoreOpenRequest;
import com.magambell.server.notification.app.port.in.request.SaveFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.in.request.SaveStoreOpenFcmTokenServiceRequest;
import com.magambell.server.notification.app.port.out.NotificationCommandPort;
import com.magambell.server.notification.app.port.out.NotificationQueryPort;
import com.magambell.server.notification.app.port.out.dto.FcmTokenDTO;
import com.magambell.server.notification.domain.entity.FcmToken;
import com.magambell.server.notification.infra.FirebaseNotificationSender;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.entity.OrderGoods;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    private final OrderQueryPort orderQueryPort;

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

    @Override
    public boolean checkUserStoreOpen(final CheckStoreOpenServiceRequest request) {
        User user = userQueryPort.findById(request.userId());
        Store store = storeQueryPort.findById(request.storeId());

        return notificationQueryPort.existsByUserAndStore(user, store);
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
            String message = "주문이 수락됐어요. [" + pickupTime.toLocalTime() + "]에 마감백을 픽업 해주세요!";
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
        tokens.forEach(token -> send("FCM 테스트",
                new FcmTokenDTO(token.getId(), token.getToken(), token.getUser().getId(), "", "")));
    }


    @Override
    public void notifyStoreOpen(final NotifyStoreOpenRequest request) {
        List<FcmTokenDTO> tokens = notificationQueryPort.findWithAllByStoreId(request.store());

        tokens.forEach(token -> {
            String nickname = token.nickName();
            String storeName = token.storeName();
            String message = "[" + nickname + "]님이 기다리던 [" + storeName + "]의 마감백 판매가 시작됐어요!";

            send(message, token);
        });
    }

    @Transactional
    @Override
    public void notifyPickup(final LocalDateTime pickupTime) {
        List<Order> orders = orderQueryPort.findOrdersToNotifyByPickupTime(pickupTime);

        Set<User> customers = orders.stream()
                .map(Order::getUser)
                .collect(Collectors.toSet());

        Set<User> owners = orders.stream()
                .flatMap(order -> order.getOrderStoreOwner().stream())
                .collect(Collectors.toSet());

        List<FcmTokenDTO> customerTokens = notificationQueryPort.findByUsers(customers);
        List<FcmTokenDTO> ownerTokens = notificationQueryPort.findByUsers(owners);

        Map<Long, FcmTokenDTO> tokenByUserId = Stream.concat(customerTokens.stream(), ownerTokens.stream())
                .collect(Collectors.toMap(
                        FcmTokenDTO::userId,
                        Function.identity(),
                        (t1, t2) -> t1
                ));

        orders.forEach(order -> {
            User customer = order.getUser();
            FcmTokenDTO customerToken = tokenByUserId.get(customer.getId());

            if (customerToken != null) {
                String storeNames = order.getOrderGoodsList().stream()
                        .map(og -> og.getGoods().getStore().getName())
                        .distinct()
                        .collect(Collectors.joining(", ")); // 여러 매장이라면 쉼표로

                String message = "[" + storeNames + "] 에서 마감백을 픽업해주세요!";
                send(message, customerToken);
            }

            order.getOrderStoreOwner().forEach(owner -> {
                FcmTokenDTO ownerToken = tokenByUserId.get(owner.getId());
                if (ownerToken != null) {
                    Set<String> storesOwnedByThisOwner = order.getOrderGoodsList().stream()
                            .map(OrderGoods::getGoods)
                            .filter(goods -> goods.getStore().getUser().equals(owner))
                            .map(goods -> goods.getStore().getName())
                            .collect(Collectors.toSet());

                    storesOwnedByThisOwner.forEach(storeName -> {
                        String message = "[" + storeName + "]의 픽업 가능 시간이 시작되었습니다.";
                        send(message, ownerToken);
                    });
                }
            });

            order.markPickupNotificationSent();
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
