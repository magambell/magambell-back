package com.magambell.server.store.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.favorite.domain.entity.Favorite;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.notification.domain.entity.FcmToken;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
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
public class Store extends BaseTimeEntity {

    @Column(name = "store_id")
    @Tsid
    @Id
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String ownerName;
    private String ownerPhone;
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    private Bank bankName;

    private String bankAccount;

    private String description;

    private String parkingDescription;

    @Enumerated(EnumType.STRING)
    private Approved approved;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Goods> goods = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImage> storeImages = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<FcmToken> fcmTokens = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Store(final String name, final String address, final Double latitude, final Double longitude,
                  final String ownerName, final String ownerPhone,
                  final String businessNumber, final Bank bankName, final String bankAccount, final Approved approved,
                  final String description, final String parkingDescription) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.businessNumber = businessNumber;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.approved = approved;
        this.description = description;
        this.parkingDescription = parkingDescription;
    }

    public static Store create(final RegisterStoreDTO dto) {
        return Store.builder()
                .name(dto.name())
                .address(dto.address())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .ownerName(dto.ownerName())
                .ownerPhone(dto.ownerPhone())
                .businessNumber(dto.businessNumber())
                .bankName(dto.bankName())
                .bankAccount(dto.bankAccount())
                .approved(dto.approved())
                .description(dto.description())
                .parkingDescription(dto.parkingDescription())
                .build();
    }

    public void addStoreImage(final StoreImage storeImage) {
        this.storeImages.add(storeImage);
        storeImage.addStore(this);
    }

    public void addUser(final User user) {
        this.user = user;
    }

    public void addGoods(final Goods goods) {
        this.goods.add(goods);
        goods.addStore(this);
    }

    public void approve() {
        this.approved = Approved.APPROVED;
    }

    public boolean isOwnedBy(final User user) {
        return this.user.getId().equals(user.getId()) && user.getUserRole() == UserRole.OWNER;
    }

    public void addFavorite(final Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void addFcmToken(final FcmToken fcmToken) {
        this.fcmTokens.add(fcmToken);
    }
}
