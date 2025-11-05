package com.magambell.server.banner.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.favorite.domain.entity.Favorite;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.notification.domain.entity.FcmToken;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.entity.StoreImage;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.user.domain.entity.User;
import com.magambell.server.user.domain.enums.UserRole;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Banner extends BaseTimeEntity {

    @Column(name = "banner_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @Column(name = "`order`")
    private Integer order;

    @Builder(access = AccessLevel.PRIVATE)
    private Banner(final String name, final Integer order) {
        this.name = name;
        this.order = order;
    }

    public static Banner create(final String name, final Integer order) {
        return Banner.builder()
                .name(name)
                .order(order)
                .build();
    }
}
