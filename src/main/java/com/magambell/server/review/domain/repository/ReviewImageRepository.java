package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
