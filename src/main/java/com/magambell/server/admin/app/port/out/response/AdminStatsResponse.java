package com.magambell.server.admin.app.port.out.response;

public record AdminStatsResponse(
        Long totalUserCount,
        Long totalStoreCount
) {
}
