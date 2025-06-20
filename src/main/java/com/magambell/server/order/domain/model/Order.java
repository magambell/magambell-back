package com.magambell.server.order.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.payment.domain.model.Payment;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private LocalDateTime pickupTime;
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderGoods> orderGoodsList = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Order(final OrderStatus orderStatus, final Integer totalPrice, final LocalDateTime pickupTime,
                  final String memo) {
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.pickupTime = pickupTime;
        this.memo = memo;
    }

    public static Order create(final CreateOrderDTO dto, final OrderStatus orderStatus) {
        Order order = Order.builder()
                .orderStatus(orderStatus)
                .totalPrice(dto.totalPrice())
                .pickupTime(dto.pickupTime())
                .build();

        OrderGoods orderGoods = dto.toOrderGoods();

        order.addUser(dto.user());
        order.addOrderGoods(orderGoods);
        orderGoods.addGoods(dto.goods());

        return order;
    }

    public void addUser(final User user) {
        this.user = user;
        user.addOrder(this);
    }

    public void addOrderGoods(final OrderGoods orderGoods) {
        this.orderGoodsList.add(orderGoods);
        orderGoods.addOrder(this);
    }

    public void addPayment(final Payment payment) {
        this.payments.add(payment);
    }
}
