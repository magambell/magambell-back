package com.magambell.server.review.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.order.domain.model.OrderGoods;
import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.user.domain.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    private Integer rating;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_goods_id")
    private OrderGoods orderGoods;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewReason> reviewReasons = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Review(final Integer rating, final String description) {
        this.rating = rating;
        this.description = description;
    }

    public static Review create(final RegisterReviewDTO dto) {
        if (dto.rating() < 2 || dto.rating() > 5) {
            throw new InvalidRequestException(ErrorCode.INVALID_REVIEW_RATING);
        }

        Review review = Review.builder()
                .rating(dto.rating())
                .description(dto.description())
                .build();

        List<ReviewReason> list = dto.satisfactionReasons().stream()
                .map(ReviewReason::new)
                .toList();

        review.addUser(dto.user());
        review.addOrderGoods(dto.orderGoods());
        review.addReviewReasons(list);

        return review;

    }

    private void addReviewReasons(final List<ReviewReason> reviewReasons) {
        this.reviewReasons.addAll(reviewReasons);
        reviewReasons.forEach(reviewReason -> reviewReason.addReview(this));
    }

    private void addOrderGoods(final OrderGoods orderGoods) {
        this.orderGoods = orderGoods;
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
