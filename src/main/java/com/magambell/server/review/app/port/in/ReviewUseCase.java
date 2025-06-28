package com.magambell.server.review.app.port.in;

import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRegisterResponseDTO;
import java.util.List;

public interface ReviewUseCase {
    ReviewRegisterResponseDTO registerReview(RegisterReviewServiceRequest request, Long userId);

    List<ReviewListDTO> getReviewList(ReviewListServiceRequest request);
}
