package com.magambell.server.order.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.order.app.port.in.OrderUseCase;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.out.OrderCommandPort;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.domain.model.StockHistory;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService implements OrderUseCase {

    private final OrderCommandPort orderCommandPort;
    private final GoodsQueryPort goodsQueryPort;
    private final UserQueryPort userQueryPort;
    private final StockCommandPort stockCommandPort;

    @Transactional
    @Override
    public void createOrder(final CreateOrderServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        Goods goods = goodsQueryPort.findByIdWithStockAndLock(request.goodsId());

        StockHistory stockHistory = goods.getStock().recordDecrease(goods, request.quantity());
        stockCommandPort.saveStockHistory(stockHistory);

        validateOrderRequest(request, goods);

        orderCommandPort.createOrder(request.toDTO(user, goods));
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
