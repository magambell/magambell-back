package com.magambell.server.order.app.port.in.dto;

import static com.magambell.server.order.domain.enums.OrderStatus.PENDING;

import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.entity.OrderGoods;
import com.magambell.server.user.domain.entity.User;
import java.time.LocalDateTime;

public record CreateOrderDTO(
        User user,
        Goods goods,
        Integer quantity,
        Integer totalPrice,
        LocalDateTime pickupTime,
        String memo
) {

    public Order toOrder() {
        return Order.create(this, PENDING);
    }

    public OrderGoods toOrderGoods() {
        return OrderGoods.create(this);
    }
}
