package com.magambell.server.store.domain.repository;

import com.magambell.server.store.domain.model.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
}
