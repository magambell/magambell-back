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
import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRatingSummaryDTO;
import com.magambell.server.user.domain.enums.UserStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
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

        return getReviewListDTOS(pageable, conditions);
    }

    @Override
    public ReviewRatingSummaryDTO getReviewRatingAll(final ReviewRatingAllServiceRequest request) {
        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(goods.id.eq(request.goodsId()));
        conditions.and(user.userStatus.eq(UserStatus.ACTIVE));
        conditions.and(order.orderStatus.eq(OrderStatus.COMPLETED));
        if (request.imageCheck()) {
            conditions.and(reviewImage.isNotNull());
        }

        NumberPath<Long> ratingCount = Expressions.numberPath(Long.class, "ratingCount");

        List<Tuple> results = queryFactory
                .select(review.rating, review.rating.count().as(ratingCount))
                .from(review)
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .innerJoin(orderGoods).on(orderGoods.id.eq(review.orderGoods.id))
                .innerJoin(order).on(order.id.eq(orderGoods.order.id))
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                .innerJoin(store).on(store.id.eq(goods.store.id))
                .innerJoin(user).on(user.id.eq(review.user.id))
                .where(conditions)
                .groupBy(review.rating)
                .fetch();

        long totalCount = 0;
        long ratingSum = 0;
        long rating2 = 0, rating3 = 0, rating4 = 0, rating5 = 0;

        for (Tuple tuple : results) {
            Integer rating = tuple.get(review.rating);
            Long count = tuple.get(ratingCount);

            if (rating != null && count != null) {
                totalCount += count;
                ratingSum += rating * count;

                switch (rating) {
                    case 2 -> rating2 = count;
                    case 3 -> rating3 = count;
                    case 4 -> rating4 = count;
                    case 5 -> rating5 = count;
                }
            }
        }

        double averageRating = totalCount > 0 ? Math.round(((double) ratingSum / totalCount) * 10.0) / 10.0 : 0.0;

        return new ReviewRatingSummaryDTO(
                averageRating,
                totalCount,
                rating2,
                rating3,
                rating4,
                rating5
        );
    }

    @Override
    public List<ReviewListDTO> getReviewListByUser(final Long userId, final Pageable pageable) {
        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(user.id.eq(userId));
        return getReviewListDTOS(pageable, conditions);
    }

    private List<ReviewListDTO> getReviewListDTOS(final Pageable pageable, final BooleanBuilder conditions) {
        return queryFactory
                .select(review, reviewImage, order, orderGoods, goods, store, user)
                .from(review)
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .leftJoin(reviewReason).on(reviewReason.review.id.eq(review.id))
                .innerJoin(orderGoods).on(orderGoods.id.eq(review.orderGoods.id))
                .innerJoin(order).on(order.id.eq(orderGoods.order.id))
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                .innerJoin(store).on(store.id.eq(goods.store.id))
                .innerJoin(user).on(user.id.eq(review.user.id))
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
                                                user.nickName,
                                                goods.id,
                                                store.id
                                        )
                                )
                );
    }
}
