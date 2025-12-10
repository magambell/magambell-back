package com.magambell.server.goods.domain.repository;

import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.goods.domain.entity.GoodsImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsImageRepository extends JpaRepository<GoodsImage, Long>, GoodsImageRepositoryCustom {

    List<GoodsImage> findByGoodsId(Long goodsId);

}
