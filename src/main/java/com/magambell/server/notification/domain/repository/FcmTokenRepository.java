package com.magambell.server.notification.domain.repository;

import com.magambell.server.notification.domain.model.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long>, FcmTokenRepositoryCustom {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    void deleteByUserId(Long userId);
}
