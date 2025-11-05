package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewReportListServiceRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReviewReportListRequest(
        @NotNull(message = "리뷰를 선택해주세요.")
        Long reviewId,
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public ReviewReportListServiceRequest toServiceRequest() {
        return new ReviewReportListServiceRequest(reviewId, page, size);
    }
}
