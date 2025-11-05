package com.magambell.server.store.app.port.out.response;

public record OpenRegionListDTO(
        String region,
        Long userId,
        String nickName
) {
}
