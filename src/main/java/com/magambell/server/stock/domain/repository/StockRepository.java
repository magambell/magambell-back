package com.magambell.server.stock.domain.repository;

import com.magambell.server.stock.domain.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {

}
