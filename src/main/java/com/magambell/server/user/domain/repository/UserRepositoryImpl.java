package com.magambell.server.user.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.order.domain.model.QOrder.order;
import static com.magambell.server.order.domain.model.QOrderGoods.orderGoods;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.user.domain.model.QUser.user;
import static com.magambell.server.user.domain.model.QUserSocialAccount.userSocialAccount;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.user.app.port.out.dto.MyPageStatsDTO;
import com.magambell.server.user.app.port.out.dto.UserInfoDTO;
import com.magambell.server.user.domain.enums.UserStatus;
import com.magambell.server.user.domain.model.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findUserBySocial(final ProviderType providerType, final String providerId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(user)
                        .join(user.userSocialAccounts, userSocialAccount).fetchJoin()
                        .where(
                                userSocialAccount.providerType.eq(providerType),
                                userSocialAccount.providerId.eq(providerId),
                                user.userStatus.eq(UserStatus.ACTIVE)
                        )
                        .fetchOne()
        );
    }

    @Override
    public UserInfoDTO getUserInfo(final Long userId) {

        return queryFactory.select(Projections.constructor(UserInfoDTO.class,
                        user.email,
                        user.nickName,
                        user.userRole,
                        userSocialAccount.providerType,
                        store.approved,
                        goods.id
                ))
                .from(user)
                .leftJoin(userSocialAccount).on(userSocialAccount.user.id.eq(user.id))
                .leftJoin(store).on(store.user.id.eq(user.id))
                .leftJoin(goods).on(goods.store.id.eq(store.id))
                .where(
                        user.id.eq(userId),
                        user.userStatus.eq(UserStatus.ACTIVE)
                )
                .fetchOne();
    }

    @Override
    public boolean existsUserBySocial(final ProviderType providerType, final String providerId) {
        Integer result = queryFactory
                .selectOne()
                .from(user)
                .join(user.userSocialAccounts, userSocialAccount)
                .where(
                        userSocialAccount.providerType.eq(providerType),
                        userSocialAccount.providerId.eq(providerId),
                        user.userStatus.eq(UserStatus.ACTIVE)
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public MyPageStatsDTO getMyPageData(final Long userId) {

        List<Tuple> tuples = queryFactory
                .select(order.id.count(), order.totalPrice.sum(), orderGoods.originalPrice.sum(),
                        orderGoods.salePrice.sum())
                .from(orderGoods)
                .innerJoin(order).on(order.id.eq(orderGoods.order.id))
                .innerJoin(user).on(user.id.eq(order.user.id))
                .where(
                        order.user.id.eq(userId)
                                .and(user.userStatus.eq(UserStatus.ACTIVE))
                                .and(order.orderStatus.eq(OrderStatus.COMPLETED))
                )
                .fetch();

        Tuple result = tuples.get(0);

        if (result == null) {
            return new MyPageStatsDTO(0, 0.0, 0L);
        }

        int purchaseCount = result.get(order.id.count()).intValue();
        long totalPaid = result.get(order.totalPrice.sum());
        long originalTotal = result.get(orderGoods.originalPrice.sum());
        long saleTotal = result.get(orderGoods.salePrice.sum());
        long savedPrice = originalTotal - saleTotal;
        double savedKg = Math.round(totalPaid * 0.0003 * 10.0) / 10.0;

        return new MyPageStatsDTO(purchaseCount, savedKg, savedPrice);
    }
}
