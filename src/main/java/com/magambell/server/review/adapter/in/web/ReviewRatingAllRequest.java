package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import jakarta.validation.constraints.NotNull;

public record ReviewRatingAllRequest(
        @NotNull(message = "상품을 선택해주세요.")
        Long goodsId,
        @NotNull(message = "이미지 유무를 선택해 주세요.")
        Boolean imageCheck
) {
    public ReviewRatingAllServiceRequest toServiceRequest() {
        return new ReviewRatingAllServiceRequest(goodsId, imageCheck);
    }
}
