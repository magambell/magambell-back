package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReviewListRequest(
        @NotNull(message = "상품을 선택해주세요.")
        Long goodsId,
        @NotNull(message = "이미지 유무를 선택해 주세요.")
        Boolean imageCheck,
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public ReviewListServiceRequest toServiceRequest() {
        return new ReviewListServiceRequest(goodsId, imageCheck, page, size);
    }
}
