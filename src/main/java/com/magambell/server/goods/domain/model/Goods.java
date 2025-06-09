package com.magambell.server.goods.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.store.domain.model.Store;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goods extends BaseTimeEntity {

    @Column(name = "goods_id")
    @Tsid
    @Id
    private Long id;

    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer originalPrice;
    private Integer discount;
    private Integer salePrice;
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(mappedBy = "goods", cascade = CascadeType.ALL)
    private Stock stock;

    @Builder(access = AccessLevel.PRIVATE)
    private Goods(final String name, final LocalTime startTime, final LocalTime endTime, final Integer originalPrice,
                  final Integer discount,
                  final Integer salePrice, final String description) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.salePrice = salePrice;
        this.description = description;
    }
}
