package com.magambell.server.order.domain.repository;

import static com.magambell.server.goods.domain.model.QGoods.goods;
import static com.magambell.server.order.domain.model.QOrder.order;
import static com.magambell.server.order.domain.model.QOrderGoods.orderGoods;
import static com.magambell.server.payment.domain.model.QPayment.payment;
import static com.magambell.server.store.domain.model.QStore.store;
import static com.magambell.server.store.domain.model.QStoreImage.storeImage;
import static com.magambell.server.user.domain.model.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.enums.UserStatus;
import com.magambell.server.user.domain.model.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                .select(order, orderGoods, goods, store, storeImage, user)
                .from(order)
                .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id))
                .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                .innerJoin(store).on(store.id.eq(goods.store.id))
                .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                .innerJoin(user).on(user.id.eq(order.user.id))
                .where(
                        user.userStatus.eq(UserStatus.ACTIVE)
                                .and(
                                        user.id.eq(userId)
                                )
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        groupBy(order.id)
                                .list(
                                        Projections.constructor(
                                                OrderListDTO.class,
                                                order.id,
                                                order.orderStatus,
                                                order.createdAt,
                                                orderGoods.quantity,
                                                orderGoods.salePrice,
                                                store.id,
                                                store.name,
                                                list(storeImage.name),
                                                goods.name
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
                                order.orderStatus,
                                store.name,
                                store.address,
                                storeImage.name.min(),
                                orderGoods.quantity,
                                order.totalPrice,
                                order.pickupTime,
                                order.memo
                        ))
                        .from(order)
                        .innerJoin(orderGoods).on(orderGoods.order.id.eq(order.id))
                        .innerJoin(goods).on(goods.id.eq(orderGoods.goods.id))
                        .innerJoin(store).on(store.id.eq(goods.store.id))
                        .leftJoin(storeImage).on(storeImage.store.id.eq(store.id))
                        .innerJoin(payment).on(payment.order.id.eq(order.id))
                        .where(
                                order.id.eq(orderId),
                                order.user.id.eq(userId),
                                order.user.userStatus.eq(UserStatus.ACTIVE)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<OrderStoreListDTO> getOrderStoreList(final Pageable pageable, final Long userId) {
        QUser owner = new QUser("owner");
        QUser customer = new QUser("customer");

        return queryFactory
                .select(order, orderGoods, goods, store, customer)
                .from(owner)
                .innerJoin(store).on(store.user.id.eq(owner.id))
                .innerJoin(goods).on(goods.store.id.eq(store.id))
                .innerJoin(orderGoods).on(orderGoods.goods.id.eq(goods.id))
                .innerJoin(order).on(order.id.eq(orderGoods.order.id))
                .innerJoin(customer).on(customer.id.eq(order.user.id))
                .where(
                        owner.id.eq(userId)
                                .and(owner.userRole.eq(UserRole.OWNER))
                                .and(owner.userStatus.eq(UserStatus.ACTIVE))
                                .and(
                                        order.orderStatus.notIn(
                                                OrderStatus.PENDING,
                                                OrderStatus.CANCELED,
                                                OrderStatus.FAILED
                                        )
                                )
                )
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
                                                orderGoods.quantity,
                                                order.totalPrice,
                                                customer.phoneNumber,
                                                goods.name
                                        )
                                )
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
}
