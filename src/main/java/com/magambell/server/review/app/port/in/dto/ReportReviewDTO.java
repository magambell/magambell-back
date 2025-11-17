package com.magambell.server.review.app.port.in.dto;


import com.magambell.server.review.domain.entity.Review;
import com.magambell.server.user.domain.entity.User;

public record ReportReviewDTO(
       Review review,
       User user
) {
}
