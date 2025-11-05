package com.magambell.server.review.app.port.out;

import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.app.port.in.dto.ReportReviewDTO;
import com.magambell.server.review.app.port.out.response.ReviewRegisterResponseDTO;
import com.magambell.server.review.domain.entity.ReviewReport;

public interface ReviewCommandPort {
    ReviewRegisterResponseDTO registerReview(RegisterReviewDTO dto);

    void reportReview(ReportReviewDTO dto);
}
