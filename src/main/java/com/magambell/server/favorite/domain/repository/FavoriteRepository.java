package com.magambell.server.favorite.domain.repository;

import com.magambell.server.favorite.domain.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
