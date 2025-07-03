package com.magambell.server.favorite.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
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
public class Favorite extends BaseTimeEntity {

    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Builder(access = AccessLevel.PRIVATE)
    private Favorite(final User user, final Store store) {
        this.user = user;
        this.store = store;
    }

    public static Favorite create(final Store store, final User user) {
        Favorite favorite = Favorite.builder()
                .user(user)
                .store(store)
                .build();
        store.addFavorite(favorite);
        user.addFavorite(favorite);
        return favorite;
    }
}
