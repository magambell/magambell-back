package com.magambell.server.review.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import com.magambell.server.review.domain.enums.ServiceSatisfaction;
import com.magambell.server.user.domain.model.User;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceSatisfaction serviceSatisfaction;

    @Enumerated(EnumType.STRING)
    private SatisfactionReason satisfactionReason;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Review(final ServiceSatisfaction serviceSatisfaction, final SatisfactionReason satisfactionReason,
                   final String description) {
        this.serviceSatisfaction = serviceSatisfaction;
        this.satisfactionReason = satisfactionReason;
        this.description = description;
    }

    public static Review create(final RegisterReviewDTO dto) {
        Review review = Review.builder()
                .serviceSatisfaction(dto.serviceSatisfaction())
                .satisfactionReason(dto.satisfactionReason())
                .description(dto.description())
                .build();

        review.addUser(dto.user());
        review.addOrder(dto.order());

        return review;

    }

    private void addOrder(final Order order) {
        this.order = order;
    }

    private void addUser(final User user) {
        this.user = user;
        user.addReview(this);
    }

    public void addReviewImage(final ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
        reviewImage.addReview(this);
    }
}
