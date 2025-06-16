package com.magambell.server.goods.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.stock.domain.model.StockHistory;
import com.magambell.server.store.domain.model.Store;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer quantity;
    private Integer originalPrice;
    private Integer discount;
    private Integer salePrice;
    private String description;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    private List<StockHistory> stockHistory = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Goods(final String name, final LocalDateTime startTime, final LocalDateTime endTime,
                  final Integer quantity,
                  final Integer originalPrice,
                  final Integer discount,
                  final Integer salePrice, final String description, final SaleStatus saleStatus) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.salePrice = salePrice;
        this.description = description;
        this.saleStatus = saleStatus;
    }

    public static Goods create(RegisterGoodsDTO dto) {
        return Goods.builder()
                .name(dto.name())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .quantity(dto.quantity())
                .originalPrice(dto.originalPrice())
                .discount(dto.discount())
                .salePrice(dto.salePrice())
                .description(dto.description())
                .saleStatus(dto.saleStatus())
                .build();
    }

    public void addStore(final Store store) {
        this.store = store;
    }

    public void addStock(final StockHistory stockHistory) {
        this.stockHistory.add(stockHistory);
        stockHistory.addGoods(this);
    }
}
