package com.magambell.server.review.app.port.in.request;

import com.magambell.server.order.domain.model.OrderGoods;
import com.magambell.server.review.adapter.in.web.ReviewImageRegister;
import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import com.magambell.server.user.domain.model.User;
import java.util.List;

public record RegisterReviewServiceRequest(
        Long orderGoodsId,
        Integer rating,
        List<SatisfactionReason> satisfactionReasons,
        String description,
        List<ReviewImageRegister> reviewImageRegisters
) {
    public RegisterReviewDTO toDto(final User user, final OrderGoods orderGoods) {
        return new RegisterReviewDTO(orderGoodsId, rating, satisfactionReasons, description,
                reviewImageRegisters, user, orderGoods);
    }
}
