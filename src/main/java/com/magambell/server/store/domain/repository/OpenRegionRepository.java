package com.magambell.server.store.domain.repository;

import com.magambell.server.store.domain.entity.OpenRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenRegionRepository extends JpaRepository<OpenRegion, Long>, OpenRegionRepositoryCustom {


    Optional<OpenRegion> findByRegion(String region);
}
