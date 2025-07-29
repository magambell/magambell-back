package com.magambell.server.order.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.order.domain.model.QOrder.order;
import static com.magambell.server.order.domain.model.QOrderGoods.orderGoods;
import static com.magambell.server.payment.domain.model.QPayment.payment;
import static com.magambell.server.review.domain.model.QReview.review;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.store.domain.model.QStoreImage.storeImage;
import static com.magambell.server.user.domain.model.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.set;

import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.enums.PickupNotificationStatus;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.order.domain.model.OrderGoods;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.enums.UserStatus;
import com.magambell.server.user.domain.model.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderListDTO> getOrderList(final Pageable pageable, final Long userId) {
        return queryFactory
                .select(order, orderGoods, goods, store, storeImage, user, review)
                .from(order)
                .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id))
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                .innerJoin(store).on(store.id.eq(goods.store.id))
                .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                .leftJoin(review).on(review.orderGoods.id.eq(orderGoods.id))
                .innerJoin(user).on(user.id.eq(order.user.id))
                .where(
                        user.userStatus.eq(UserStatus.ACTIVE)
                                .and(
                                        user.id.eq(userId)
                                )
                                .and(
                                        order.orderStatus.ne(OrderStatus.PENDING)
                                )
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(order.id)
                                .list(
                                        Projections.constructor(OrderListDTO.class,
                                                order.id,
                                                order.orderStatus,
                                                order.createdAt,
                                                order.memo,
                                                store.id,
                                                store.name,
                                                set(storeImage.name),
                                                list(Projections.constructor(OrderListDTO.OrderGoodsInfo.class,
                                                        orderGoods.id,
                                                        goods.name,
                                                        orderGoods.quantity,
                                                        orderGoods.salePrice
                                                )),
                                                set(review.id)
                                        )
                                )
                );
    }

    @Override
    public Optional<OrderDetailDTO> getOrderDetail(final Long orderId, final Long userId) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                OrderDetailDTO.class,
                                order.id,
                                orderGoods.id,
                                order.orderStatus,
                                store.name,
                                store.address,
                                storeImage.name.min(),
                                orderGoods.quantity,
                                order.totalPrice,
                                order.pickupTime,
                                order.memo,
                                order.createdAt,
                                store.id,
                                review.id.min()
                        ))
                        .from(order)
                        .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id))
                        .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                        .innerJoin(store).on(store.id.eq(goods.store.id))
                        .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                        .innerJoin(payment).on(payment.order.id.eq(order.id))
                        .leftJoin(review).on(review.orderGoods.id.eq(orderGoods.id))
                        .where(
                                order.id.eq(orderId),
                                order.user.id.eq(userId),
                                order.user.userStatus.eq(UserStatus.ACTIVE)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<OrderStoreListDTO> getOrderStoreList(final Pageable pageable, final Long userId,
                                                     final OrderStatus orderStatus) {
        QUser owner = new QUser("owner");
        QUser customer = new QUser("customer");

        BooleanBuilder where = new BooleanBuilder();
        where.and(owner.id.eq(userId))
                .and(owner.userRole.eq(UserRole.OWNER))
                .and(owner.userStatus.eq(UserStatus.ACTIVE))
                .and(buildOrderStatusCondition(orderStatus));

        return queryFactory
                .select(order, orderGoods, goods, store, customer)
                .from(owner)
                .innerJoin(store).on(store.user.id.eq(owner.id))
                .innerJoin(goods).on(goods.store.id.eq(store.id))
                .innerJoin(orderGoods).on(orderGoods.goods.id.eq(goods.id))
                .innerJoin(order).on(order.id.eq(orderGoods.order.id))
                .innerJoin(customer).on(customer.id.eq(order.user.id))
                .where(where)
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(order.id)
                                .list(
                                        Projections.constructor(
                                                OrderStoreListDTO.class,
                                                order.id,
                                                order.orderStatus,
                                                order.createdAt,
                                                order.pickupTime,
                                                order.memo,
                                                orderGoods.quantity,
                                                order.totalPrice,
                                                customer.phoneNumber,
                                                goods.name
                                        )
                                )
                );
    }

    @Override
    public Optional<Order> findOwnerWithAllById(final Long orderId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(order)
                        .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id)).fetchJoin()
                        .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id)).fetchJoin()
                        .innerJoin(store).on(store.id.eq(goods.store.id)).fetchJoin()
                        .innerJoin(payment).on(payment.order.id.eq(order.id)).fetchJoin()
                        .innerJoin(user).on(user.id.eq(store.user.id)).fetchJoin()
                        .where(
                                order.id.eq(orderId)
                                        .and(user.userRole.eq(UserRole.OWNER))
                                        .and(user.userStatus.eq(UserStatus.ACTIVE))
                                        .and(store.approved.eq(Approved.APPROVED))
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<Order> findWithAllById(final Long orderId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(order)
                        .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id)).fetchJoin()
                        .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id)).fetchJoin()
                        .innerJoin(store).on(store.id.eq(goods.store.id)).fetchJoin()
                        .innerJoin(payment).on(payment.order.id.eq(order.id)).fetchJoin()
                        .innerJoin(user).on(user.id.eq(order.user.id)).fetchJoin()
                        .where(
                                order.id.eq(orderId)
                                        .and(user.userRole.eq(UserRole.CUSTOMER))
                                        .and(user.userStatus.eq(UserStatus.ACTIVE))
                                        .and(store.approved.eq(Approved.APPROVED))
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<OrderGoods> findOrderGoodsWithOrderById(final Long orderGoodsId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(orderGoods)
                        .innerJoin(order).on(order.id.eq(orderGoods.order.id)).fetchJoin()
                        .where(orderGoods.id.eq(orderGoodsId))
                        .fetchOne()
        );
    }

    @Override
    public List<Order> findOrdersToNotifyByPickupTime(final LocalDateTime pickupTime) {
        QUser owner = new QUser("owner");
        QUser customer = new QUser("customer");
        return queryFactory
                .selectFrom(order)
                .distinct()
                .innerJoin(customer).on(customer.id.eq(order.user.id)).fetchJoin()
                .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id)).fetchJoin()
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id)).fetchJoin()
                .innerJoin(store).on(store.id.eq(goods.store.id)).fetchJoin()
                .innerJoin(owner).on(owner.id.eq(store.user.id)).fetchJoin()
                .where(
                        order.pickupTime.eq(pickupTime),
                        order.pickupNotificationStatus.eq(PickupNotificationStatus.NOT_SENT),
                        customer.userStatus.eq(UserStatus.ACTIVE),
                        owner.userStatus.eq(UserStatus.ACTIVE)
                )
                .fetch();
    }

    @Override
    public List<Order> findByPaidProcessedOrders(final LocalDateTime pickupTime, final LocalDateTime createdAtCutOff) {
        return queryFactory
                .selectFrom(order)
                .distinct()
                .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id)).fetchJoin()
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id)).fetchJoin()
                .innerJoin(store).on(store.id.eq(goods.store.id)).fetchJoin()
                .innerJoin(payment).on(payment.order.id.eq(order.id)).fetchJoin()
                .where(
                        order.orderStatus.eq(OrderStatus.PAID),
                        goods.startTime.eq(pickupTime), // 9시이고 지금은 8시 30분이지만 픽업 시작 시간이 9시
                        order.pickupTime.eq(pickupTime), // 주문 픽업시간 설정한게 9시
                        order.createdAt.loe(createdAtCutOff) // 생성된게 <= 35분전 8시 25분
                )
                .fetch();
    }

    @Override
    public List<Order> findByAutoRejectProcessedOrders(final LocalDateTime minusMinutes, final LocalDateTime pickupTime,
                                                       final LocalDateTime createdAtCutOff) {
        return queryFactory
                .selectFrom(order)
                .distinct()
                .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id)).fetchJoin()
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id)).fetchJoin()
                .innerJoin(store).on(store.id.eq(goods.store.id)).fetchJoin()
                .innerJoin(payment).on(payment.order.id.eq(order.id)).fetchJoin()
                .where(
                        order.orderStatus.eq(OrderStatus.PAID),
                        order.pickupTime.eq(pickupTime), // 9시
                        goods.startTime.loe(pickupTime), // 9시
                        order.createdAt.gt(createdAtCutOff), // 9시 일때 8시 25분
                        order.createdAt.loe(minusMinutes) // 실제 시간이 8시 30분이라면 5분이고
                )
                .fetch();
    }

    private BooleanBuilder buildOrderStatusCondition(OrderStatus orderStatus) {
        BooleanBuilder builder = new BooleanBuilder();

        if (orderStatus != null) {
            return builder.and(order.orderStatus.eq(orderStatus));
        }

        return builder.and(order.orderStatus.notIn(
                OrderStatus.PENDING,
                OrderStatus.CANCELED,
                OrderStatus.FAILED
        ));
    }
}
