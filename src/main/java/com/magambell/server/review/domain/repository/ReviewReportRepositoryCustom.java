package com.magambell.server.review.domain.repository;

import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewReportListServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRatingSummaryDTO;
import com.magambell.server.review.app.port.out.response.ReviewReportListDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewReportRepositoryCustom {

    List<ReviewReportListDTO> getReviewReportList(ReviewReportListServiceRequest request, Pageable pageable);

}
