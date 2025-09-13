package com.magambell.server.order.app.service;

import static com.magambell.server.payment.domain.enums.PaymentStatus.READY;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.common.exception.UnauthorizedException;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.order.app.port.in.OrderUseCase;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.in.request.CustomerOrderListServiceRequest;
import com.magambell.server.order.app.port.in.request.OwnerOrderListServiceRequest;
import com.magambell.server.order.app.port.in.request.RejectOrderServiceRequest;
import com.magambell.server.order.app.port.out.OrderCommandPort;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;
import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.enums.RejectReason;
import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.app.port.out.PaymentCommandPort;
import com.magambell.server.payment.app.port.out.PortOnePort;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.enums.PaymentCompletionType;
import com.magambell.server.stock.app.port.in.StockUseCase;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.app.port.out.StockQueryPort;
import com.magambell.server.stock.domain.entity.Stock;
import com.magambell.server.stock.domain.entity.StockHistory;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService implements OrderUseCase {

    private static final List<OrderStatus> OWNER_LIST_VALID_STATUSES = List.of(
            OrderStatus.PAID,
            OrderStatus.ACCEPTED,
            OrderStatus.COMPLETED
    );

    private final OrderCommandPort orderCommandPort;
    private final OrderQueryPort orderQueryPort;
    private final GoodsQueryPort goodsQueryPort;
    private final UserQueryPort userQueryPort;
    private final StockCommandPort stockCommandPort;
    private final PaymentCommandPort paymentCommandPort;
    private final StockQueryPort stockQueryPort;
    private final StockUseCase stockUseCase;
    private final PortOnePort portOnePort;
    private final NotificationUseCase notificationUseCase;

    @Transactional
    @Override
    public CreateOrderResponseDTO createOrder(final CreateOrderServiceRequest request, final Long userId,
                                              final LocalDateTime now) {
        User user = userQueryPort.findById(userId);
        Goods goods = goodsQueryPort.findById(request.goodsId());
        Stock stock = stockQueryPort.findByGoodsIdWithLock(goods.getId());
        StockHistory stockHistory = stock.recordDecrease(goods, request.quantity());
        stockCommandPort.saveStockHistory(stockHistory);

        validateOrderRequest(request, goods, now);

        Order order = orderCommandPort.createOrder(request.toDTO(user, goods));

        Payment payment = paymentCommandPort.createReadyPayment(
                new CreatePaymentDTO(order,
                        request.totalPrice(),
                        READY)
        );

        return new CreateOrderResponseDTO(payment.getMerchantUid(), payment.getAmount());
    }

    @Override
    public List<OrderListDTO> getOrderList(final CustomerOrderListServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        return orderQueryPort.getOrderList(PageRequest.of(request.page() - 1, request.size()), user.getId());
    }

    @Override
    public OrderDetailDTO getOrderDetail(final Long orderId, final Long userId) {
        User user = userQueryPort.findById(userId);
        return orderQueryPort.getOrderDetail(orderId, user.getId());
    }

    @Override
    public List<OrderStoreListDTO> getOrderStoreList(final OwnerOrderListServiceRequest request, final Long userId) {
        if (request.orderStatus() != null && !OWNER_LIST_VALID_STATUSES.contains(request.orderStatus())) {
            throw new InvalidRequestException(ErrorCode.INVALID_ORDER_STATUS);
        }
        User user = userQueryPort.findById(userId);
        return orderQueryPort.getOrderStoreList(PageRequest.of(request.page() - 1, request.size()), user.getId()
                , request.orderStatus());
    }

    @Transactional
    @Override
    public void approveOrder(final Long orderId, final Long userId, final LocalDateTime now) {
        User user = userQueryPort.findById(userId);
        Order order = orderQueryPort.findOwnerWithAllById(orderId);
        validateApproveOrder(user, order, now);
        order.accepted();

        notificationUseCase.notifyApproveOrder(order.getUser(), order.getPickupTime());
    }

    @Transactional
    @Override
    public void rejectOrder(final RejectOrderServiceRequest request) {
        User user = userQueryPort.findById(request.userId());
        Order order = orderQueryPort.findOwnerWithAllById(request.orderId());

        validateRejectOrder(user, order);
        order.rejected(request.rejectReason());

        Payment payment = order.getPayment();
        stockUseCase.restoreStockIfNecessary(payment);
        portOnePort.cancelPayment(payment.getMerchantUid(), order.getTotalPrice(), "사장님 주문 취소");
        notificationUseCase.notifyRejectOrder(order.getUser());
    }

    @Transactional
    @Override
    public void cancelOrder(final Long orderId, final Long userId) {
        User user = userQueryPort.findById(userId);
        Order order = orderQueryPort.findWithAllById(orderId);

        validateCancelOrder(user, order);
        order.cancelled(PaymentCompletionType.REDIRECT);

        Payment payment = order.getPayment();
        stockUseCase.restoreStockIfNecessary(payment);
        portOnePort.cancelPayment(payment.getMerchantUid(), order.getTotalPrice(), "고객님 주문 취소");
    }

    @Transactional
    @Override
    public void completedOrder(final Long orderId, final Long userId) {
        User user = userQueryPort.findById(userId);
        Order order = orderQueryPort.findOwnerWithAllById(orderId);

        validateCompletedOrder(user, order);
        order.completed();
    }

    @Transactional
    @Override
    public void batchRejectOrdersBeforePickup(final LocalDateTime pickupTime, final LocalDateTime createdAtCutOff) {
        List<Order> orders = orderQueryPort.findByPaidBeforePickupRejectProcessedOrders(pickupTime, createdAtCutOff);
        orders.forEach(order -> {
            log.info("[픽업 30분 전] 시스템 주문 거절 order = {}", order.getId());
            order.rejected(RejectReason.SYSTEM);
            Payment payment = order.getPayment();
            stockUseCase.restoreStockIfNecessary(payment);
            portOnePort.cancelPayment(payment.getMerchantUid(), order.getTotalPrice(), "시스템 주문 취소");
            notificationUseCase.notifyRejectOrder(order.getUser());
        });
    }

    @Transactional
    @Override
    public void autoRejectOrdersAfter(final LocalDateTime minusMinutes, final LocalDateTime pickupTime) {
        List<Order> orders = orderQueryPort.findByAutoRejectProcessedOrders(minusMinutes,
                pickupTime);
        orders.forEach(order -> {
            log.info("[5분 마다] 시스템 주문 거절 order = {}", order.getId());
            order.rejected(RejectReason.SYSTEM);
            Payment payment = order.getPayment();
            stockUseCase.restoreStockIfNecessary(payment);
            portOnePort.cancelPayment(payment.getMerchantUid(), order.getTotalPrice(), "시스템 주문 취소");
            notificationUseCase.notifyRejectOrder(order.getUser());
        });
    }

    private void validateOrderRequest(final CreateOrderServiceRequest request, final Goods goods,
                                      final LocalDateTime now) {
        if (request.totalPrice() != goods.getSalePrice() * request.quantity()) {
            throw new InvalidRequestException(ErrorCode.INVALID_TOTAL_PRICE);
        }

        if (request.pickupTime().isBefore(goods.getStartTime()) || request.pickupTime().isAfter(goods.getEndTime())) {
            throw new InvalidRequestException(ErrorCode.INVALID_PICKUP_TIME);
        }

        if (!request.pickupTime().isAfter(now)) {
            throw new InvalidRequestException(ErrorCode.INVALID_NOW_TIME_PICKUP_TIME);
        }
    }

    private void validateApproveOrder(final User user, final Order order, final LocalDateTime now) {
        if (!order.isOwner(user)) {
            throw new UnauthorizedException(ErrorCode.ORDER_NO_ACCESS);
        }

        validateOrderForDecision(order);

        if (order.getPickupTime().isBefore(now)) {
            throw new InvalidRequestException(ErrorCode.INVALID_PICKUP_TIME);
        }
    }

    private void validateRejectOrder(final User user, final Order order) {
        if (!order.isOwner(user)) {
            throw new UnauthorizedException(ErrorCode.ORDER_NO_ACCESS);
        }

        validateOrderForDecision(order);
    }

    private void validateCancelOrder(final User user, final Order order) {
        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            throw new UnauthorizedException(ErrorCode.ORDER_NO_ACCESS);
        }

        validateOrderForDecision(order);
    }

    private void validateCompletedOrder(final User user, final Order order) {
        if (!order.isOwner(user)) {
            throw new UnauthorizedException(ErrorCode.ORDER_NO_ACCESS);
        }

        if (order.getOrderStatus() != OrderStatus.ACCEPTED) {
            throw new InvalidRequestException(ErrorCode.ORDER_NOT_APPROVED);
        }
    }

    private void validateOrderForDecision(final Order order) {
        if (order.getOrderStatus() == OrderStatus.ACCEPTED) {
            throw new InvalidRequestException(ErrorCode.ORDER_ALREADY_ACCEPTED);
        }

        if (order.getOrderStatus() == OrderStatus.REJECTED) {
            throw new InvalidRequestException(ErrorCode.ORDER_ALREADY_REJECTED);
        }

        if (order.getOrderStatus() != OrderStatus.PAID) {
            throw new InvalidRequestException(ErrorCode.INVALID_PAYMENT_STATUS_PAID);
        }
    }

}
