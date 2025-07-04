package com.magambell.server.notification.domain.repository;

import com.magambell.server.notification.domain.model.FcmToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    List<FcmToken> findByStoreId(Long storeId);
}
