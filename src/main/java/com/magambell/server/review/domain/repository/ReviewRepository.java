package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.enums.ReviewStatus;
import com.magambell.server.review.domain.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    boolean existsByOrderGoodsIdAndUserIdAndReviewStatus(Long orderGoodsId, Long userId, ReviewStatus reviewStatus);

    Optional<Review> findByIdAndUserIdAndReviewStatus(Long reviewId, Long userId, ReviewStatus reviewStatus);

    Optional<Review> findByIdAndReviewStatus(Long reviewId, ReviewStatus reviewStatus);
}
