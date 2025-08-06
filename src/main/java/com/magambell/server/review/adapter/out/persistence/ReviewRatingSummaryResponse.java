package com.magambell.server.review.adapter.out.persistence;

public record ReviewRatingSummaryResponse(
        Double averageRating,
        Long totalCount,
        Long rating2Count,
        Long rating3Count,
        Long rating4Count,
        Long rating5Count
) {
}
