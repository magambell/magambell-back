package com.magambell.server.review.adapter.out.persistence;

public record ReviewRatingSummaryResponse(
        Double averageRating,
        Long totalCount,
        Long rating1Count,
        Long rating2Count,
        Long rating3Count
) {
}
