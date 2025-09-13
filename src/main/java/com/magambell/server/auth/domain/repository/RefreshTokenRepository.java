package com.magambell.server.auth.domain.repository;

import com.magambell.server.auth.domain.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUserId(Long userId);

    Optional<RefreshToken> findByUserId(Long userId);
}
