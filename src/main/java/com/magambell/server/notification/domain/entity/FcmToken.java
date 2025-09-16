package com.magambell.server.notification.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmToken extends BaseTimeEntity {

    @Column(name = "fcm_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = true)
    private Store store;

    @Builder(access = AccessLevel.PRIVATE)
    private FcmToken(final String token) {
        this.token = token;
    }

    public static FcmToken create(final String token, final User user) {
        FcmToken fcmToken = FcmToken.builder()
                .token(token)
                .build();

        fcmToken.addUser(user);
        return fcmToken;
    }

    public static FcmToken create(final String token, final User user, final Store store) {
        FcmToken fcmToken = create(token, user);
        fcmToken.addStore(store);

        return fcmToken;
    }

    private void addUser(final User user) {
        this.user = user;
        user.addFcmToken(this);
    }

    private void addStore(final Store store) {
        this.store = store;
        store.addFcmToken(this);
    }
}
