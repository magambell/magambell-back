package com.magambell.server.review.app.port.out;

import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.app.port.out.response.ReviewRegisterResponseDTO;

public interface ReviewCommandPort {
    ReviewRegisterResponseDTO registerReview(RegisterReviewDTO dto);
}
