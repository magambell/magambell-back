package com.magambell.server.review.domain.repository;

import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    List<ReviewListDTO> getReviewList(ReviewListServiceRequest request, Pageable pageable);
}
