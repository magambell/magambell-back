package com.magambell.server.region.domain.repository;

import com.magambell.server.region.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * 시·도 목록 조회 (중복 제거)
     */
    @Query("SELECT DISTINCT r.sido FROM Region r WHERE r.isDeleted = false ORDER BY r.sido")
    List<String> findDistinctSido();

    /**
     * 특정 시·도의 시·군·구 목록 조회 (중복 제거, NULL 제외)
     */
    @Query("SELECT DISTINCT r.sigungu FROM Region r WHERE r.sido = :sido AND r.sigungu IS NOT NULL AND r.sigungu != '' AND r.isDeleted = false ORDER BY r.sigungu")
    List<String> findDistinctSigunguBySido(String sido);

    /**
     * 특정 시·군·구의 읍·면·동 목록 조회 (중복 제거, NULL 제외)
     */
    @Query("SELECT DISTINCT r.eupmyeondong FROM Region r WHERE r.sido = :sido AND r.sigungu = :sigungu AND r.eupmyeondong IS NOT NULL AND r.eupmyeondong != '' AND r.isDeleted = false ORDER BY r.eupmyeondong")
    List<String> findDistinctEupmyeondongBySidoAndSigungu(String sido, String sigungu);
}
