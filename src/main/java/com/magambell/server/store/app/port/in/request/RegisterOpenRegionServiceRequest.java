package com.magambell.server.store.app.port.in.request;

import com.magambell.server.store.app.port.in.dto.OpenRegionDTO;
import com.magambell.server.user.domain.entity.User;

public record RegisterOpenRegionServiceRequest(
        String region
) {
    public RegisterOpenRegionServiceRequest(final String region) {
        this.region = region;
    }

    public OpenRegionDTO toOpenRegionDTO(final String region, final User user) {
        return new OpenRegionDTO(region, user);
    }
}
