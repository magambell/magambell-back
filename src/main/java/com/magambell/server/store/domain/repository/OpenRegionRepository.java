package com.magambell.server.store.domain.repository;

import com.magambell.server.region.domain.entity.Region;
import com.magambell.server.store.domain.entity.OpenRegion;
import com.magambell.server.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenRegionRepository extends JpaRepository<OpenRegion, Long>, OpenRegionRepositoryCustom {

    @Deprecated // 기존 데이터 호환성용
    Optional<OpenRegion> findByRegion(String region);

    // 특정 사용자가 특정 지역을 이미 요청했는지 확인
    Optional<OpenRegion> findByUserAndRegionEntity(User user, Region region);
}
