package com.magambell.server.review.domain.repository;

import com.magambell.server.review.app.port.in.request.ReviewReportListServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewReportListDTO;
import com.magambell.server.review.domain.entity.ReviewReport;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewReportRepositoryCustom {

    List<ReviewReportListDTO> getReviewReportList(ReviewReportListServiceRequest request, Pageable pageable);

    ReviewReport getReviewReportByReviewIdAndUserId(Long reviewId, Long userId);

}
