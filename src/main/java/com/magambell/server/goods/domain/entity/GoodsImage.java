package com.magambell.server.goods.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Builder
@AllArgsConstructor
public class GoodsImage extends BaseTimeEntity {

    @Column(name = "goods_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    private String goodsName;

    private String imageUrl;
}
