package com.magambell.server.store.app.port.in.dto;

import com.magambell.server.user.domain.entity.User;

public record OpenRegionDTO(
        String region,
        User user
) {

}
