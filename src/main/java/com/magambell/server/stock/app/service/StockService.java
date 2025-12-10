package com.magambell.server.stock.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.order.domain.entity.OrderGoods;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.stock.app.port.in.StockUseCase;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.app.port.out.StockQueryPort;
import com.magambell.server.stock.domain.entity.Stock;
import com.magambell.server.stock.domain.entity.StockHistory;
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
        // 결제가 완료되지 않은 경우 재고 복구 불필요 (재고 차감이 없었음)
        if (!payment.isPaid()) {
            return;
        }
        
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
