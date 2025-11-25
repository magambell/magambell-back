package com.magambell.server.goods.domain.repository;

import com.magambell.server.goods.domain.entity.GoodsImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsImageRepository extends JpaRepository<GoodsImage, Long>, GoodsImageRepositoryCustom {

}
