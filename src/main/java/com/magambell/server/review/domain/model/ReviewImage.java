package com.magambell.server.review.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewImage extends BaseTimeEntity {

    @Column(name = "review_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @Column(name = "`order`")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewImage(final String name, final Integer order) {
        this.name = name;
        this.order = order;
    }

    public static ReviewImage create(final String name, final Integer order) {
        return ReviewImage.builder()
                .name(name)
                .order(order)
                .build();
    }

    public void addReview(final Review review) {
        this.review = review;
    }
}
