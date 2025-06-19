package com.magambell.server.goods.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.stock.domain.model.QStock.stock;

import com.magambell.server.goods.domain.model.Goods;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Goods> findByIdWithStockAndLock(final Long goodsId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(goods)
                        .join(goods.stock, stock).fetchJoin()
                        .where(goods.id.eq(goodsId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }
}
