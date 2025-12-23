package com.magambell.server.region.app.port.out;

import com.magambell.server.region.domain.entity.Region;

public interface RegionQueryPort {
    
    /**
     * region_id로 Region 조회
     */
    Region findById(Long regionId);
}
