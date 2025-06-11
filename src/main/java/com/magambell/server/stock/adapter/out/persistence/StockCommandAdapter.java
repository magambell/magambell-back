package com.magambell.server.stock.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.stock.app.port.out.StockCommandPort;
import com.magambell.server.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class StockCommandAdapter implements StockCommandPort {

    private final StockRepository stockRepository;
}
