package com.magambell.server.review.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import com.magambell.server.review.app.port.out.ReviewQueryPort;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRatingSummaryDTO;
import com.magambell.server.review.domain.repository.ReviewRepository;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Adapter
public class ReviewQueryAdapter implements ReviewQueryPort {

    private final ReviewRepository reviewRepository;

    @Override
    public boolean existsOrderAndReview(final Order order, final User user) {
        return reviewRepository.existsByOrderIdAndUserId(order.getId(), user.getId());
    }

    @Override
    public List<ReviewListDTO> getReviewList(final ReviewListServiceRequest request, final Pageable pageable) {
        return reviewRepository.getReviewList(request, pageable);
    }

    @Override
    public ReviewRatingSummaryDTO getReviewRatingAll(final ReviewRatingAllServiceRequest request) {
        return reviewRepository.getReviewRatingAll(request);
    }

    @Override
    public List<ReviewListDTO> getReviewListByUser(final User user, final Pageable pageable) {
        return reviewRepository.getReviewListByUser(user.getId(), pageable);
    }
}
