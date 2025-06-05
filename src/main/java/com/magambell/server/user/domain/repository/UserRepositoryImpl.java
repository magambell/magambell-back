package com.magambell.server.user.domain.repository;

import static com.magambell.server.user.domain.model.QUser.user;
import static com.magambell.server.user.domain.model.QUserSocialAccount.userSocialAccount;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.domain.model.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                        .where(userSocialAccount.providerType.eq(providerType),
                                userSocialAccount.providerId.eq(providerId))
                        .fetchOne()
        );
    }
}
