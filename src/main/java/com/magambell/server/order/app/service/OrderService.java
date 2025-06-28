package com.magambell.server.order.app.service;

import static com.magambell.server.payment.domain.enums.PaymentStatus.READY;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.order.app.port.in.OrderUseCase;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.out.OrderCommandPort;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;
import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.app.port.out.PaymentCommandPort;
import com.magambell.server.payment.domain.model.Payment;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.app.port.out.StockQueryPort;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.stock.domain.model.StockHistory;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public List<OrderListDTO> getOrderList(final Long userId) {
        User user = userQueryPort.findById(userId);
        return orderQueryPort.getOrderList(user.getId());
    }

    @Override
    public OrderDetailDTO getOrderDetail(final Long orderId, final Long userId) {
        User user = userQueryPort.findById(userId);
        return orderQueryPort.getOrderDetail(orderId, user.getId());
    }

    private void validateOrderRequest(final CreateOrderServiceRequest request, final Goods goods) {
        if (request.totalPrice() != goods.getSalePrice() * request.quantity()) {
            throw new InvalidRequestException(ErrorCode.INVALID_TOTAL_PRICE);
        }

        if (request.pickupTime().isBefore(goods.getStartTime()) || request.pickupTime().isAfter(goods.getEndTime())) {
            throw new InvalidRequestException(ErrorCode.INVALID_PICKUP_TIME);
        }
    }

}
