package com.magambell.server.goods.adapter.in.web;

import com.magambell.server.goods.app.port.in.request.ChangeGoodsStatusServiceRequest;
import com.magambell.server.goods.domain.enums.SaleStatus;

public record ChangeGoodsStatusRequest(
        Long goodsId,
        SaleStatus saleStatus
) {

    public ChangeGoodsStatusServiceRequest toService(Long userId) {
        return new ChangeGoodsStatusServiceRequest(goodsId, saleStatus, userId);
    }
}
