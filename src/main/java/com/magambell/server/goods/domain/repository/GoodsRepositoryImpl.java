package com.magambell.server.goods.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.stock.domain.model.QStock.stock;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.user.domain.model.QUser.user;

import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Goods> findWithStoreAndUserById(final Long goodsId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(goods)
                .innerJoin(store).on(store.id.eq(goods.store.id)).fetchJoin()
                .innerJoin(user).on(store.user.id.eq(user.id)).fetchJoin()
                .innerJoin(stock).on(stock.goods.id.eq(goods.id)).fetchJoin()
                .where(
                        goods.id.eq(goodsId)
                                .and(store.approved.eq(Approved.APPROVED))
                                .and(user.userStatus.eq(UserStatus.ACTIVE))
                )
                .fetchOne()
        );
    }
}
