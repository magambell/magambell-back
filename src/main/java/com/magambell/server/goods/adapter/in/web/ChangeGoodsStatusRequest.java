package com.magambell.server.goods.adapter.in.web;

import com.magambell.server.goods.app.port.in.request.ChangeGoodsStatusServiceRequest;
import com.magambell.server.goods.domain.enums.SaleStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeGoodsStatusRequest(
        @NotNull(message = "상품을 선택해 주세요.")
        Long goodsId,
        @NotNull(message = "판매 상태를 선택해 주세요.")
        SaleStatus saleStatus
) {

    public ChangeGoodsStatusServiceRequest toService(Long userId) {
        return new ChangeGoodsStatusServiceRequest(goodsId, saleStatus, userId);
    }
}
