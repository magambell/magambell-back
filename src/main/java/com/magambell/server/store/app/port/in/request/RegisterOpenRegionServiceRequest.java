package com.magambell.server.store.app.port.in.request;

import com.magambell.server.region.domain.entity.Region;
import com.magambell.server.store.app.port.in.dto.OpenRegionDTO;
import com.magambell.server.user.domain.entity.User;

public record RegisterOpenRegionServiceRequest(
        Long regionId
) {
    public RegisterOpenRegionServiceRequest(final Long regionId) {
        this.regionId = regionId;
    }

    public OpenRegionDTO toOpenRegionDTO(final Region region, final User user) {
        return new OpenRegionDTO(region, user);
    }
}
