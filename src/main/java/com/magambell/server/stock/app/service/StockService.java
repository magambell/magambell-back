package com.magambell.server.stock.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.order.domain.model.OrderGoods;
import com.magambell.server.payment.domain.model.Payment;
import com.magambell.server.stock.app.port.in.StockUseCase;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.app.port.out.StockQueryPort;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.stock.domain.model.StockHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StockService implements StockUseCase {

    private final StockCommandPort stockCommandPort;
    private final StockQueryPort stockQueryPort;

    @Transactional
    @Override
    public void restoreStockIfNecessary(final Payment payment) {
        OrderGoods orderGoods = payment.getOrder().getOrderGoodsList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Goods goods = orderGoods.getGoods();

        Stock stock = stockQueryPort.findByGoodsIdWithLock(goods.getId());
        StockHistory stockHistory = stock.restoreCancel(goods, orderGoods.getQuantity());
        stockCommandPort.saveStockHistory(stockHistory);
    }
}
