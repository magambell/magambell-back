package com.magambell.server.store.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.stock.domain.model.QStock.stock;
import static com.magambell.server.store.domain.enums.Approved.APPROVED;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.store.domain.model.QStoreImage.storeImage;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.dsl.Expressions.allOf;

import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.enums.SearchSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private static final Integer LIMIT_KM = 5;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreListDTOResponse> getStoreList(final SearchStoreListServiceRequest request) {
        NumberExpression<Double> distance = null;

        if (request.latitude() != null && request.longitude() != null
                && request.latitude() != 0.0 && request.longitude() != 0.0) {
            distance = Expressions.numberTemplate(
                    Double.class,
                    "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                    request.latitude(),
                    store.latitude,
                    request.longitude(),
                    store.longitude
            );
        }

        return queryFactory.select(store, storeImage, goods, stock)
                .from(store)
                .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                .leftJoin(goods).on(goods.store.id.eq(store.id))
                .innerJoin(stock).on(stock.goods.id.eq(goods.id))
                .where(
                        allOf(
                                radiusCondition(request, distance),
                                availableNowCondition(request),
                                keywordCondition(request.keyword())
                        )
                                .and(store.approved.eq(APPROVED))
                )
                .orderBy(sortCondition(request.sortType(), distance))
                .transform(
                        groupBy(store.id)
                                .list(Projections.constructor(StoreListDTOResponse.class,
                                        store.id,
                                        store.name,
                                        list(storeImage.name),
                                        goods.name,
                                        goods.startTime,
                                        goods.endTime,
                                        goods.originalPrice,
                                        goods.discount,
                                        goods.salePrice,
                                        stock.quantity,
                                        distance != null ? distance : Expressions.nullExpression(Double.class),
                                        goods.saleStatus
                                ))
                );
    }

    @Override
    public Optional<StoreDetailDTO> getStoreDetail(final Long storeId) {
        Map<Long, StoreDetailDTO> result = queryFactory.select(store, storeImage, goods, stock)
                .from(store)
                .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                .leftJoin(goods).on(goods.store.id.eq(store.id))
                .leftJoin(stock).on(stock.goods.id.eq(goods.id))
                .where(
                        store.id.eq(storeId)
                                .and(
                                        store.approved.eq(APPROVED)
                                )
                )
                .transform(
                        groupBy(store.id).as(
                                Projections.constructor(StoreDetailDTO.class,
                                        store.id,
                                        goods.id,
                                        store.name,
                                        store.address,
                                        list(storeImage.name),
                                        goods.startTime,
                                        goods.endTime,
                                        goods.originalPrice,
                                        goods.salePrice,
                                        goods.discount,
                                        goods.description,
                                        stock.quantity,
                                        goods.saleStatus
                                )
                        )
                );
        return Optional.ofNullable(result.get(storeId));
    }

    private BooleanExpression keywordCondition(final String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return store.name.containsIgnoreCase(keyword);
        }
        return null;
    }

    private BooleanExpression radiusCondition(SearchStoreListServiceRequest request,
                                              NumberExpression<Double> distance) {
        if (distance != null) {
            return distance.loe(LIMIT_KM);
        }
        return null;
    }

    private BooleanExpression availableNowCondition(SearchStoreListServiceRequest request) {
        if (Boolean.TRUE.equals(request.onlyAvailable())) {
            return stock.quantity.gt(0);
        }
        return null;
    }

    private OrderSpecifier<?> sortCondition(SearchSortType sortType, NumberExpression<Double> distance) {
        if (sortType == null) {
            return store.createdAt.desc();
        }
        if (sortType == SearchSortType.DISTANCE_ASC) {
            if (distance == null) {
                return store.createdAt.desc();
            }
            return distance.asc();
        }
        if (sortType == SearchSortType.PRICE_ASC) {
            return goods.salePrice.asc();
        }
        return store.createdAt.desc();
    }
}
