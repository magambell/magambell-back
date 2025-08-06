package com.magambell.server.stock.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.stock.domain.enums.StockType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
public class StockHistory extends BaseTimeEntity {

    @Column(name = "stock_history_id")
    @Tsid
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private StockType stockType;

    private Integer beforeQuantity;

    private Integer quantity;

    private Integer resultQuantity;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Builder(access = AccessLevel.PRIVATE)
    private StockHistory(StockType stockType, final Integer beforeQuantity, final Integer quantity,
                         final Integer resultQuantity) {
        this.stockType = stockType;
        this.beforeQuantity = beforeQuantity;
        this.quantity = quantity;
        this.resultQuantity = resultQuantity;
    }

    public static StockHistory create(StockType stockType, Integer beforeQuantity, Integer quantity,
                                      Integer resultQuantity) {
        return StockHistory.builder()
                .stockType(stockType)
                .beforeQuantity(beforeQuantity)
                .quantity(quantity)
                .resultQuantity(resultQuantity)
                .build();
    }

    public void addGoods(final Goods goods) {
        this.goods = goods;
    }
}
