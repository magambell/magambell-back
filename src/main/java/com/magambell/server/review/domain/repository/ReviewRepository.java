package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderIdAndUserId(Long orderId, Long userId);
}
