package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import jakarta.validation.constraints.NotNull;

public record ReviewListRequest(
        @NotNull(message = "상품을 선택해주세요.")
        Long goodsId,

        Boolean imageCheck
) {
    public ReviewListServiceRequest toServiceRequest() {
        return new ReviewListServiceRequest(goodsId, imageCheck);
    }
}
