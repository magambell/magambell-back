package com.magambell.server.review.app.port.in.request;

import com.magambell.server.order.domain.model.Order;
import com.magambell.server.review.adapter.in.web.ReviewImageRegister;
import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import com.magambell.server.review.domain.enums.ServiceSatisfaction;
import com.magambell.server.user.domain.model.User;
import java.util.List;

public record RegisterReviewServiceRequest(
        Long orderId,
        ServiceSatisfaction serviceSatisfaction,
        SatisfactionReason satisfactionReason,
        String description,
        List<ReviewImageRegister> reviewImageRegisters
) {
    public RegisterReviewDTO toDto(final User user, final Order order) {
        return new RegisterReviewDTO(orderId, serviceSatisfaction, satisfactionReason, description,
                reviewImageRegisters, user, order);
    }
}
