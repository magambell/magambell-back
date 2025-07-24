package com.magambell.server.review.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.order.domain.model.OrderGoods;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import com.magambell.server.review.app.port.out.ReviewQueryPort;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRatingSummaryDTO;
import com.magambell.server.review.domain.enums.ReviewStatus;
import com.magambell.server.review.domain.model.Review;
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
    public boolean existsOrderAndReview(final OrderGoods orderGoods, final User user) {
        return reviewRepository.existsByOrderGoodsIdAndUserIdAndReviewStatus(orderGoods.getId(), user.getId(),
                ReviewStatus.ACTIVE);
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

    @Override
    public Review findByIdAndUserId(final Long reviewId, final Long userId) {
        return reviewRepository.findByIdAndUserIdAndReviewStatus(reviewId, userId, ReviewStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
