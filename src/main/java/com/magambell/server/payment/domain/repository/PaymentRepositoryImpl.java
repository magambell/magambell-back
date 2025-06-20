package com.magambell.server.payment.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.order.domain.model.QOrder.order;
import static com.magambell.server.order.domain.model.QOrderGoods.orderGoods;
import static com.magambell.server.payment.domain.model.QPayment.payment;
import static com.magambell.server.stock.domain.model.QStock.stock;

import com.magambell.server.payment.domain.model.Payment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Payment> findByMerchantUidJoinOrder(final String merchantUid) {
        return Optional.ofNullable(
                queryFactory.selectFrom(payment)
                        .join(payment.order, order).fetchJoin()
                        .join(order.orderGoodsList, orderGoods).fetchJoin()
                        .join(orderGoods.goods, goods).fetchJoin()
                        .join(goods.stock, stock).fetchJoin()
                        .where(payment.merchantUid.eq(merchantUid))
                        .fetchOne()
        );
    }
}
