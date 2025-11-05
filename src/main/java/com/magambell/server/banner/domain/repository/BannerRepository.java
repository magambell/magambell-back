package com.magambell.server.banner.domain.repository;

import com.magambell.server.banner.domain.entity.Banner;
import com.magambell.server.store.domain.entity.OpenRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom {

}
