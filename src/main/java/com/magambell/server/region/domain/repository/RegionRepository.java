package com.magambell.server.region.domain.repository;

import com.magambell.server.region.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * 시·도 목록 조회 (중복 제거)
     */
    @Query("SELECT DISTINCT r.city FROM Region r WHERE r.isDeleted = false ORDER BY r.city")
    List<String> findDistinctCity();

    /**
     * 특정 시·도의 시·군·구 목록 조회 (중복 제거, NULL 제외)
     */
    @Query("SELECT DISTINCT r.district FROM Region r WHERE r.city = :city AND r.district IS NOT NULL AND r.district != '' AND r.isDeleted = false ORDER BY r.district")
    List<String> findDistinctDistrictByCity(String city);

    /**
     * 특정 시·군·구의 읍·면·동 목록 조회 (중복 제거, NULL 제외)
     */
    @Query("SELECT DISTINCT r.town FROM Region r WHERE r.city = :city AND r.district = :district AND r.town IS NOT NULL AND r.town != '' AND r.isDeleted = false ORDER BY r.town")
    List<String> findDistinctTownByCityAndDistrict(String city, String district);
}
