package com.magambell.server.stock.domain.repository;

import com.magambell.server.stock.domain.model.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockHistory, Long> {
}
