package com.magambell.server.store.domain.repository;

import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    boolean existsByUser(User user);

    Optional<Store> findByUser(User user);
}
