package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.goods.domain.enums.SaleStatus;

public record ChangeGoodsStatusServiceRequest(Long goodsId, SaleStatus saleStatus, Long userId) {
}
