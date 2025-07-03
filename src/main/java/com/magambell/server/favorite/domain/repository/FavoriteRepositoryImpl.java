package com.magambell.server.favorite.domain.repository;

import static com.magambell.server.favorite.domain.model.QFavorite.favorite;
import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.stock.domain.model.QStock.stock;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.store.domain.model.QStoreImage.storeImage;
import static com.magambell.server.user.domain.model.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FavoriteStoreListDTOResponse> getFavoriteStoreList(final Pageable pageable, final Long userId) {
        return queryFactory.select(store, storeImage, goods, stock, favorite, user)
                .from(store)
                .innerJoin(favorite).on(favorite.store.id.eq(store.id))
                .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                .leftJoin(goods).on(goods.store.id.eq(store.id))
                .innerJoin(stock).on(stock.goods.id.eq(goods.id))
                .innerJoin(user).on(store.user.id.eq(user.id))
                .where(
                        favorite.user.id.eq(userId)
                                .and(store.approved.eq(Approved.APPROVED))
                                .and(user.userStatus.eq(UserStatus.ACTIVE))
                )
                .orderBy(favorite.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(store.id)
                                .list(Projections.constructor(FavoriteStoreListDTOResponse.class,
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
                                                goods.saleStatus
                                        )
                                )
                );
    }
}
