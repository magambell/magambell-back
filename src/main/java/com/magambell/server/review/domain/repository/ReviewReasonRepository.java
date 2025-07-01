package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.model.ReviewReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReasonRepository extends JpaRepository<ReviewReason, Long> {
}
