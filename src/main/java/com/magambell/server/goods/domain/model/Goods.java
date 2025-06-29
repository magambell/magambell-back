package com.magambell.server.goods.domain.model;

import static com.magambell.server.goods.domain.enums.SaleStatus.OFF;
import static com.magambell.server.goods.domain.enums.SaleStatus.ON;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.stock.domain.model.StockHistory;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
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
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
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
    private Integer originalPrice;
    private Integer discount;
    private Integer salePrice;
    private String description;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(mappedBy = "goods", cascade = CascadeType.ALL)
    private Stock stock;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    private List<StockHistory> stockHistory = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Goods(final String name, final LocalDateTime startTime, final LocalDateTime endTime,
                  final Integer originalPrice,
                  final Integer discount,
                  final Integer salePrice, final String description, final SaleStatus saleStatus) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.salePrice = salePrice;
        this.description = description;
        this.saleStatus = saleStatus;
    }

    public static Goods create(RegisterGoodsDTO dto) {
        Goods goods = Goods.builder()
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .originalPrice(dto.originalPrice())
                .discount(dto.discount())
                .salePrice(dto.salePrice())
                .description(dto.description())
                .saleStatus(OFF)
                .build();
        Stock stock = Stock.create(dto.quantity());
        StockHistory stockHistory = dto.toStock();
        goods.addStock(stock);
        goods.addStockHistory(stockHistory);
        return goods;
    }

    public void addStore(final Store store) {
        this.store = store;
    }

    public void addStock(final Stock stock) {
        this.stock = stock;
        stock.addGoods(this);
    }

    public void decreaseStock(int amount) {
        this.stock.decrease(amount);
    }

    public int getStockQuantity() {
        return this.stock.getQuantity();
    }

    public void addStockHistory(final StockHistory stockHistory) {
        this.stockHistory.add(stockHistory);
        stockHistory.addGoods(this);
    }

    public void changeStatus(final User user, final SaleStatus saleStatus, final LocalDate today) {
        if (!this.store.isOwnedBy(user)) {
            throw new InvalidRequestException(ErrorCode.INVALID_GOODS_OWNER);
        }
        if (saleStatus == ON) {
            checkStock();
            adjustDatesToTodayIfNeeded(today);
        }
        this.saleStatus = saleStatus;
    }

    private void checkStock() {
        if (this.stock.getQuantity() == 0) {
            throw new InvalidRequestException(ErrorCode.STOCK_NOT_ENOUGH);
        }
    }

    private void adjustDatesToTodayIfNeeded(final LocalDate today) {
        if (!this.startTime.toLocalDate().equals(today)) {
            this.startTime = LocalDateTime.of(today, this.startTime.toLocalTime());
        }

        if (!this.endTime.toLocalDate().equals(today)) {
            this.endTime = LocalDateTime.of(today, this.endTime.toLocalTime());
        }
    }
}
