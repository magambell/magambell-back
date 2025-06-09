package com.magambell.server.store.domain.repository;

import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByUser(User user);
}
