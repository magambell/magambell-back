package com.magambell.server.review.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.review.app.port.in.dto.ReportReviewDTO;
import com.magambell.server.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewReport extends BaseTimeEntity {

    @Column(name = "review_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder(access = AccessLevel.PRIVATE)
    public ReviewReport(Review review, User user) {
        this.review = review;
        this.user = user;
    }

    public static ReviewReport create(final ReportReviewDTO dto) {
        return ReviewReport.builder()
                .review(dto.review())
                .user(dto.user())
                .build();
    }
}
