package com.magambell.server.stock.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.goods.domain.model.Goods;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseTimeEntity {

    @Column(name = "stock_id")
    @Tsid
    @Id
    private Long id;

    private Integer quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Builder(access = AccessLevel.PRIVATE)
    private Stock(final Integer quantity) {
        this.quantity = quantity;
    }

    public static Stock create(Integer quantity) {
        return Stock.builder()
                .quantity(quantity)
                .build();
    }

    public void addGoods(final Goods goods) {
        this.goods = goods;
    }
}
