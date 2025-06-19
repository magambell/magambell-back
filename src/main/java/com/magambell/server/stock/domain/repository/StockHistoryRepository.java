package com.magambell.server.stock.domain.repository;

import com.magambell.server.stock.domain.model.StockHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    List<StockHistory> findByGoodsId(Long goodsId);
}
