package com.magambell.server.stock.domain.repository;

import static com.magambell.server.stock.domain.model.QStock.stock;

import com.magambell.server.stock.domain.model.Stock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Stock> findByGoodsIdWithLock(final Long goodsId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(stock)
                        .where(stock.goods.id.eq(goodsId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }
}
