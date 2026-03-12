package com.magambell.server.store.domain.repository;

import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    boolean existsByUser(User user);

    Optional<Store> findByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Store s WHERE s.user = :user")
    Optional<Store> findByUserWithLock(@Param("user") User user);
}
