package com.magambell.server.order.app.service;

import static com.magambell.server.payment.domain.enums.PaymentStatus.READY;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.common.exception.UnauthorizedException;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.order.app.port.in.OrderUseCase;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.in.request.CustomerOrderListServiceRequest;
import com.magambell.server.order.app.port.in.request.OwnerOrderListServiceRequest;
import com.magambell.server.order.app.port.out.OrderCommandPort;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;
import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.app.port.out.PaymentCommandPort;
import com.magambell.server.payment.app.port.out.PortOnePort;
import com.magambell.server.payment.domain.model.Payment;
import com.magambell.server.stock.app.port.in.StockUseCase;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.app.port.out.StockQueryPort;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.stock.domain.model.StockHistory;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService implements OrderUseCase {

    private final OrderCommandPort orderCommandPort;
    private final OrderQueryPort orderQueryPort;
    private final GoodsQueryPort goodsQueryPort;
    private final UserQueryPort userQueryPort;
    private final StockCommandPort stockCommandPort;
    private final PaymentCommandPort paymentCommandPort;
    private final StockQueryPort stockQueryPort;
    private final StockUseCase stockUseCase;
    private final PortOnePort portOnePort;

    @Transactional
    @Override
    public CreateOrderResponseDTO createOrder(final CreateOrderServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        Goods goods = goodsQueryPort.findById(request.goodsId());
        Stock stock = stockQueryPort.findByGoodsIdWithLock(goods.getId());
        StockHistory stockHistory = stock.recordDecrease(goods, request.quantity());
        stockCommandPort.saveStockHistory(stockHistory);

        validateOrderRequest(request, goods);

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
        User user = userQueryPort.findById(userId);
        return orderQueryPort.getOrderStoreList(PageRequest.of(request.page() - 1, request.size()), user.getId());
    }

    @Transactional
    @Override
    public void approveOrder(final Long orderId, final Long userId, final LocalDateTime now) {
        User user = userQueryPort.findById(userId);
        Order order = orderQueryPort.findWithAllById(orderId);
        validateApproveOrder(user, order, now);
        order.accepted();
    }

    @Transactional
    @Override
    public void rejectOrder(final Long orderId, final Long userId) {
        User user = userQueryPort.findById(userId);
        Order order = orderQueryPort.findWithAllById(orderId);

        validateRejectOrder(user, order);
        order.rejected();

        Payment payment = order.getPayment();
        stockUseCase.restoreStockIfNecessary(payment);
        portOnePort.cancelPayment(payment.getMerchantUid(), order.getTotalPrice(), "사장님 주문 취소");
    }

    private void validateOrderRequest(final CreateOrderServiceRequest request, final Goods goods) {
        if (request.totalPrice() != goods.getSalePrice() * request.quantity()) {
            throw new InvalidRequestException(ErrorCode.INVALID_TOTAL_PRICE);
        }

        if (request.pickupTime().isBefore(goods.getStartTime()) || request.pickupTime().isAfter(goods.getEndTime())) {
            throw new InvalidRequestException(ErrorCode.INVALID_PICKUP_TIME);
        }
    }

    private void validateApproveOrder(final User user, final Order order, final LocalDateTime now) {
        validateOrderForDecision(user, order);

        if (order.getPickupTime().isBefore(now)) {
            throw new InvalidRequestException(ErrorCode.INVALID_PICKUP_TIME);
        }
    }

    private void validateRejectOrder(final User user, final Order order) {
        validateOrderForDecision(user, order);
    }

    private void validateOrderForDecision(final User user, final Order order) {
        if (!order.isOwner(user)) {
            throw new UnauthorizedException(ErrorCode.ORDER_NO_ACCESS);
        }

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
