package com.magambell.server.review.app.port.in.request;

public record ReviewReportListServiceRequest(
        Long reviewId,
        Integer page,
        Integer size
) {
}
