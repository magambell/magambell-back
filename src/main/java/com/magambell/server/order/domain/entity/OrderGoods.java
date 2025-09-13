package com.magambell.server.order.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
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
public class OrderGoods extends BaseTimeEntity {

    @Column(name = "order_goods_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String goodsName;
    private Integer quantity;
    private Integer originalPrice;
    private Integer salePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @Builder(access = AccessLevel.PRIVATE)
    private OrderGoods(final String goodsName, final Integer quantity, final Integer originalPrice,
                       final Integer salePrice) {
        this.goodsName = goodsName;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
    }

    public static OrderGoods create(final CreateOrderDTO dto) {
        return OrderGoods.builder()
                .goodsName(dto.goods().getName())
                .quantity(dto.quantity())
                .originalPrice(dto.goods().getOriginalPrice())
                .salePrice(dto.goods().getSalePrice())
                .build();
    }

    public void addOrder(final Order order) {
        this.order = order;
    }

    public void addGoods(final Goods goods) {
        this.goods = goods;
    }
}
