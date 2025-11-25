package com.magambell.server.goods.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.magambell.server.goods.domain.entity.QGoodsImage.goodsImage;

@RequiredArgsConstructor
public class GoodsImageRepositoryImpl implements GoodsImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public void deleteGoodsImageByGoodsId(Long goodsId) {
        queryFactory.delete(goodsImage)
                .where(goodsImage.goods.id.eq(goodsId))
                .execute();
    }

}
