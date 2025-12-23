package com.magambell.server.store.app.port.in.dto;

import com.magambell.server.region.domain.entity.Region;
import com.magambell.server.user.domain.entity.User;

public record OpenRegionDTO(
        Region region,
        User user
) {

}
