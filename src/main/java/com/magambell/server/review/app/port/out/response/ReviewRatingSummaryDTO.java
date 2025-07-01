package com.magambell.server.review.app.port.out.response;

public record ReviewRatingSummaryDTO(
        Double averageRating,
        Long totalCount,
        Long rating2Count,
        Long rating3Count,
        Long rating4Count,
        Long rating5Count
) {
}
