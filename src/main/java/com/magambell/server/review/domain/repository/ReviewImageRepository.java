package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.model.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
