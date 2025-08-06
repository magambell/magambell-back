package com.magambell.server.review.app.port.out.response;

import com.magambell.server.review.adapter.out.persistence.ReviewRatingSummaryResponse;

public record ReviewRatingSummaryDTO(
        Double averageRating,
        Long totalCount,
        Long rating2Count,
        Long rating3Count,
        Long rating4Count,
        Long rating5Count
) {
    public ReviewRatingSummaryResponse toResponse() {
        return new ReviewRatingSummaryResponse(averageRating, totalCount, rating2Count, rating3Count, rating4Count,
                rating5Count);
    }
}
