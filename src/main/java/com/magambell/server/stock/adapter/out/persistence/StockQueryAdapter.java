package com.magambell.server.stock.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.stock.app.port.out.StockQueryPort;
import com.magambell.server.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class StockQueryAdapter implements StockQueryPort {

    private final StockRepository stockRepository;

}
