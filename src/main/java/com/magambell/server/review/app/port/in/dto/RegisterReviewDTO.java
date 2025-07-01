package com.magambell.server.review.app.port.in.dto;

import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.review.adapter.in.web.ReviewImageRegister;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import com.magambell.server.user.domain.model.User;
import java.util.List;

public record RegisterReviewDTO(
        Long orderId,
        Integer rating,
        List<SatisfactionReason> satisfactionReasons,
        String description,
        List<ReviewImageRegister> reviewImageRegisters,
        User user,
        Order order
) {
    public List<ImageRegister> toImage() {
        return reviewImageRegisters.stream()
                .map(image -> new ImageRegister(image.id(), image.key()))
                .toList();
    }
}
