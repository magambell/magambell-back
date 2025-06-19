package com.magambell.server.payment.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.payment.domain.enums.PayType;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "payment_id")
    private Long id;

    private String impUid;
    private String merchantUid;

    @Enumerated(EnumType.STRING)
    private PayType payType;
    private Integer amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paid_at;
    private String failReason;
    private String cancelReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
