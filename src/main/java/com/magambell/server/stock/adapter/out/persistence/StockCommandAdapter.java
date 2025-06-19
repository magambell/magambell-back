package com.magambell.server.stock.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.domain.model.StockHistory;
import com.magambell.server.stock.domain.repository.StockHistoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class StockCommandAdapter implements StockCommandPort {

    private final StockHistoryRepository stockHistoryRepository;

    @Override
    public void saveStockHistory(final StockHistory stockHistory) {
        stockHistoryRepository.save(stockHistory);
    }
}
