package com.magambell.server.review.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewReason extends BaseTimeEntity {

    @Column(name = "review_reason_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private SatisfactionReason satisfactionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewReason(final SatisfactionReason satisfactionReason) {
        this.satisfactionReason = satisfactionReason;
    }

    public void addReview(final Review review) {
        this.review = review;
    }
}
