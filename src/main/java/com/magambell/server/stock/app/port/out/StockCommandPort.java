package com.magambell.server.stock.app.port.out;

import com.magambell.server.stock.domain.entity.StockHistory;

public interface StockCommandPort {
    void saveStockHistory(StockHistory stockHistory);
}
