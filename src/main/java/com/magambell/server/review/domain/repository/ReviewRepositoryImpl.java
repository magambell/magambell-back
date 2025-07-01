package com.magambell.server.review.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.order.domain.model.QOrder.order;
import static com.magambell.server.order.domain.model.QOrderGoods.orderGoods;
import static com.magambell.server.review.domain.model.QReview.review;
import static com.magambell.server.review.domain.model.QReviewImage.reviewImage;
import static com.magambell.server.review.domain.model.QReviewReason.reviewReason;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.user.domain.model.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.user.domain.enums.UserStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<ReviewListDTO> getReviewList(final ReviewListServiceRequest request, final Pageable pageable) {
        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(goods.id.eq(request.goodsId()));
        conditions.and(user.userStatus.eq(UserStatus.ACTIVE));
        conditions.and(order.orderStatus.eq(OrderStatus.COMPLETED));
        if (request.imageCheck()) {
            conditions.and(reviewImage.isNotNull());
        }

        return queryFactory
                .select(review, reviewImage, order, orderGoods, goods, store, user)
                .from(review)
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .leftJoin(reviewReason).on(reviewReason.review.id.eq(review.id))
                .innerJoin(order).on(order.id.eq(review.order.id))
                .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id))
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                .innerJoin(store).on(store.id.eq(goods.store.id))
                .innerJoin(user).on(user.id.eq(store.user.id))
                .where(conditions)
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(review.id)
                                .list(
                                        Projections.constructor(
                                                ReviewListDTO.class,
                                                review.id,
                                                review.rating,
                                                list(reviewReason.satisfactionReason),
                                                review.description,
                                                review.createdAt,
                                                list(reviewImage.name),
                                                goods.id,
                                                store.id
                                        )
                                )
                );
    }
}
