package com.magambell.server.stock.domain.repository;

import com.magambell.server.stock.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {

}
