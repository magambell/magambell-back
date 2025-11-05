package com.magambell.server.review.domain.repository;

import com.magambell.server.review.domain.entity.Review;
import com.magambell.server.review.domain.entity.ReviewReport;
import com.magambell.server.review.domain.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long>, ReviewReportRepositoryCustom {

}
