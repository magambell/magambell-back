package com.magambell.server.review.app.port.out.response;

import com.magambell.server.review.adapter.out.persistence.ReviewRatingSummaryResponse;

public record ReviewRatingSummaryDTO(
        Double averageRating,
        Long totalCount,
        Long rating1Count,
        Long rating2Count,
        Long rating3Count
) {
    public ReviewRatingSummaryResponse toResponse() {
        return new ReviewRatingSummaryResponse(averageRating, totalCount, rating1Count, rating2Count, rating3Count);
    }
}
