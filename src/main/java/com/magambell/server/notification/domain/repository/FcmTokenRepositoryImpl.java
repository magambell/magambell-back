package com.magambell.server.notification.domain.repository;

import static com.magambell.server.notification.domain.model.QFcmToken.fcmToken;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.user.domain.model.QUser.user;

import com.magambell.server.notification.app.port.out.dto.FcmTokenDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FcmTokenRepositoryImpl implements FcmTokenRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<FcmTokenDTO> findWithAllByStoreId(final Long storeId) {
        return queryFactory
                .select(
                        Projections.constructor(FcmTokenDTO.class,
                                fcmToken.id,
                                fcmToken.token,
                                user.nickName,
                                store.name
                        )
                )
                .from(fcmToken)
                .innerJoin(user).on(user.id.eq(fcmToken.user.id)).fetchJoin()
                .innerJoin(store).on(store.id.eq(fcmToken.store.id)).fetchJoin()
                .where(fcmToken.store.id.eq(storeId))
                .fetch();
    }
}
