package com.magambell.server.user.adapter.out.persistence;

public record MyPageStatsResponse(
        int purchaseCount,
        double savedKg,
        long savedPrice
) {
}
