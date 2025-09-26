package com.magambell.server.payment.domain.repository;

import static com.magambell.server.goods.domain.entity.QGoods.goods;
import static com.magambell.server.order.domain.entity.QOrder.order;
import static com.magambell.server.order.domain.entity.QOrderGoods.orderGoods;
import static com.magambell.server.payment.domain.entity.QPayment.payment;
import static com.magambell.server.stock.domain.entity.QStock.stock;
import static com.magambell.server.store.domain.entity.QStore.store;
import static com.magambell.server.user.domain.entity.QUser.user;

import com.magambell.server.payment.domain.entity.Payment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Payment> findByMerchantUidWithLockAndRelations(final String merchantUid) {
        Payment lockedPayment = queryFactory.selectFrom(payment)
                .where(payment.merchantUid.eq(merchantUid))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        if (lockedPayment == null) {
            return Optional.empty();
        }

        queryFactory.selectFrom(payment)
                .join(payment.order, order).fetchJoin()
                .join(order.orderGoodsList, orderGoods).fetchJoin()
                .join(orderGoods.goods, goods).fetchJoin()
                .join(goods.stock, stock).fetchJoin()
                .join(goods.store, store).fetchJoin()
                .join(store.user, user).fetchJoin()
                .where(payment.merchantUid.eq(merchantUid))
                .fetchOne();

        return Optional.of(lockedPayment);
    }
}
