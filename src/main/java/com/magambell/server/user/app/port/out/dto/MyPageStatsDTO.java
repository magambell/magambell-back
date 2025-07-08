package com.magambell.server.user.app.port.out.dto;

public record MyPageStatsDTO(
        int purchaseCount,
        double savedKg,
        long savedPrice
) {
}
