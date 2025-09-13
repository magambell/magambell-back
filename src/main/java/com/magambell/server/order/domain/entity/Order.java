package com.magambell.server.order.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.enums.PickupNotificationStatus;
import com.magambell.server.order.domain.enums.RejectReason;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.enums.PaymentCompletionType;
import com.magambell.server.user.domain.entity.User;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @Column(name = "order_id")
    @Tsid
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private LocalDateTime pickupTime;
    @Enumerated(EnumType.STRING)
    private PickupNotificationStatus pickupNotificationStatus;
    @Enumerated(EnumType.STRING)
    private RejectReason rejectReason;
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderGoods> orderGoodsList = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(final OrderStatus orderStatus, final Integer totalPrice, final LocalDateTime pickupTime,
                  final PickupNotificationStatus pickupNotificationStatus,
                  final String memo) {
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.pickupTime = pickupTime;
        this.pickupNotificationStatus = pickupNotificationStatus;
        this.memo = memo;
    }

    public static Order create(final CreateOrderDTO dto, final OrderStatus orderStatus) {
        Order order = Order.builder()
                .orderStatus(orderStatus)
                .totalPrice(dto.totalPrice())
                .pickupTime(dto.pickupTime())
                .pickupNotificationStatus(PickupNotificationStatus.NOT_SENT)
                .memo(dto.memo())
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
        this.payment = payment;
    }

    public void paid() {
        this.orderStatus = OrderStatus.PAID;
    }

    public void accepted() {
        this.orderStatus = OrderStatus.ACCEPTED;
    }

    public void rejected(final RejectReason rejectReason) {
        this.orderStatus = OrderStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.payment.cancel(PaymentCompletionType.REDIRECT);
    }

    public void completed() {
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public void cancelled(final PaymentCompletionType paymentCompletionType) {
        this.orderStatus = OrderStatus.CANCELED;
        this.payment.cancel(paymentCompletionType);
    }

    public void failed() {
        this.orderStatus = OrderStatus.FAILED;
    }

    public boolean isOwner(final User user) {
        return this.getOrderGoodsList().get(0)
                .getGoods()
                .getStore()
                .getUser()
                .equals(user);
    }

    public Set<User> getOrderStoreOwner() {
        return orderGoodsList.stream()
                .map(orderGoods -> orderGoods.getGoods().getStore().getUser())
                .collect(Collectors.toSet());
    }

    public void markPickupNotificationSent() {
        this.pickupNotificationStatus = PickupNotificationStatus.SENT;
    }
}
