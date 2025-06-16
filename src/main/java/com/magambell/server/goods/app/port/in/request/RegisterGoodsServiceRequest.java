package com.magambell.server.goods.app.port.in.request;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.store.domain.model.Store;
import java.time.LocalDateTime;

public record RegisterGoodsServiceRequest(
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer quantity,
        Integer originalPrice,
        Integer discount,
        Integer salePrice,
        SaleStatus saleStatus
) {

    public RegisterGoodsServiceRequest(final String name, final String description, final LocalDateTime startTime,
                                       final LocalDateTime endTime, final Integer quantity,
                                       final Integer originalPrice, final Integer discount, final Integer salePrice,
                                       final SaleStatus saleStatus) {
        if (!startTime.isBefore(endTime)) {
            throw new InvalidRequestException(ErrorCode.TIME_VALID);
        }
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.salePrice = salePrice;
        this.saleStatus = saleStatus;
    }

    public RegisterGoodsDTO toDTO(Store store) {
        return new RegisterGoodsDTO(name, startTime, endTime, quantity, originalPrice, discount, salePrice, description,
                store, saleStatus);
    }
}
