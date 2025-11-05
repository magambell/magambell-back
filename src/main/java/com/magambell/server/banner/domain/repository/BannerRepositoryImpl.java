package com.magambell.server.banner.domain.repository;

import com.magambell.server.store.app.port.in.request.OpenRegionListServiceRequest;
import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.magambell.server.store.domain.entity.QOpenRegion.openRegion;
import static com.magambell.server.user.domain.entity.QUser.user;

@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
