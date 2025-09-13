package com.magambell.server.order.domain.repository;

import com.magambell.server.order.domain.entity.OrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderGoodsRepository extends JpaRepository<OrderGoods, Long> {
}
