package com.magambell.server.order.app.port.in.request;

import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.user.domain.model.User;
import java.time.LocalDateTime;

public record CreateOrderServiceRequest(
        Long goodsId,
        Integer quantity,
        Integer totalPrice,
        LocalDateTime pickupTime,
        String memo
) {
    public CreateOrderDTO toDTO(final User user, final Goods goods) {
        return new CreateOrderDTO(user, goods, quantity, totalPrice, pickupTime, memo);
    }
}
