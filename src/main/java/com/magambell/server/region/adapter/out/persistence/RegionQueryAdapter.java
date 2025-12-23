package com.magambell.server.region.adapter.out.persistence;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.region.app.port.out.RegionQueryPort;
import com.magambell.server.region.domain.entity.Region;
import com.magambell.server.region.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegionQueryAdapter implements RegionQueryPort {

    private final RegionRepository regionRepository;

    @Override
    public Region findById(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_REGION));
    }
}
