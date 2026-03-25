package com.magambell.server.favorite.domain.repository;

import static com.magambell.server.favorite.domain.entity.QFavorite.favorite;
import static com.magambell.server.goods.domain.entity.QGoods.goods;
import static com.magambell.server.stock.domain.entity.QStock.stock;
import static com.magambell.server.store.domain.entity.QStore.store;
import static com.magambell.server.store.domain.entity.QStoreImage.storeImage;
import static com.magambell.server.user.domain.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FavoriteStoreListDTOResponse> getFavoriteStoreList(final Pageable pageable, final Long userId) {
        List<Long> storeIds = queryFactory
                .select(favorite.store.id)
                .from(favorite)
                .innerJoin(store).on(favorite.store.id.eq(store.id))
                .innerJoin(user).on(store.user.id.eq(user.id))
                .where(
                        favorite.user.id.eq(userId)
                                .and(store.approved.eq(Approved.APPROVED))
                                .and(user.userStatus.eq(UserStatus.ACTIVE))
                )
                .orderBy(favorite.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (storeIds.isEmpty()) {
            return List.of();
        }

        Map<Long, FavoriteStoreListDTOResponse> favoriteMap = queryFactory
                .select(store, storeImage, goods, stock)
                .from(store)
                .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                .leftJoin(goods).on(goods.store.id.eq(store.id))
                .innerJoin(stock).on(stock.goods.id.eq(goods.id))
                .where(store.id.in(storeIds))
                .transform(
                        groupBy(store.id)
                                .as(Projections.constructor(FavoriteStoreListDTOResponse.class,
                                                store.id,
                                                store.name,
                                                store.address,
                                                list(storeImage.name),
                                                goods.name,
                                                goods.startTime,
                                                goods.endTime,
                                                goods.originalPrice,
                                                goods.discount,
                                                goods.salePrice,
                                                stock.quantity,
                                                goods.saleStatus
                                        )
                                )
                );

        return storeIds.stream()
                .map(favoriteMap::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
