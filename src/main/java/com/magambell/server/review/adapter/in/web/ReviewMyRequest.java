package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.ReviewMyServiceRequest;
import jakarta.validation.constraints.Positive;

public record ReviewMyRequest(
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public ReviewMyServiceRequest toServiceRequest(final Long userId) {
        return new ReviewMyServiceRequest(page, size, userId);
    }
}
