package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    boolean existsByOrderGoodsIdAndUserId(Long orderGoodsId, Long userId);
}
