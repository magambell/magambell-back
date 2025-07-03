package com.magambell.server.favorite.domain.repository;

import com.magambell.server.favorite.domain.model.Favorite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    Optional<Favorite> findByUserIdAndStoreId(Long userId, Long storeId);
}
