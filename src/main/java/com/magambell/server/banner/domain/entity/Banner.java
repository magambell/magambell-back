package com.magambell.server.banner.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Banner extends BaseTimeEntity {

    @Column(name = "banner_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String url;

    @Column(name = "`order`")
    private Integer order;

    @Builder(access = AccessLevel.PRIVATE)
    private Banner(final String url, final Integer order) {
        this.url = url;
        this.order = order;
    }

    public static Banner create(final Integer order) {
        return Banner.builder()
                .order(order)
                .build();
    }

    public void modifyUrl(final String url) {
        this.url = url;
    }

    public void modifyOrder(final Integer order) {
        this.order = order;
    }
}
