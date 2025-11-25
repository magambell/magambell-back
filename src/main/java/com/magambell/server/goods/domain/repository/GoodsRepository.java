package com.magambell.server.goods.domain.repository;

import com.magambell.server.goods.domain.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long>, GoodsRepositoryCustom {
    boolean existsByStoreId(Long storeId);

}
