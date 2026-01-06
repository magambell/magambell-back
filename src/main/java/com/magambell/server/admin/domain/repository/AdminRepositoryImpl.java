package com.magambell.server.admin.domain.repository;

import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.enums.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.magambell.server.store.domain.entity.QStore.store;
import static com.magambell.server.user.domain.entity.QUser.user;

@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countActiveUsers() {
        return queryFactory
                .select(user.count())
                .from(user)
                .where(
                        user.userRole.eq(UserRole.CUSTOMER),
                        user.userStatus.eq(UserStatus.ACTIVE)
                )
                .fetchOne();
    }

    @Override
    public Long countApprovedStores() {
        return queryFactory
                .select(store.count())
                .from(store)
                .where(
                        store.approved.eq(Approved.APPROVED)
                )
                .fetchOne();
    }
}
