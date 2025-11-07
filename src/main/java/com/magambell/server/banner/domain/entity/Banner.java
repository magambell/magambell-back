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
