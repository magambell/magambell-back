package com.magambell.server.appversion.domain.repository;

import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionPolicyRepository extends JpaRepository<AppVersionPolicy, Long> {
    
    Optional<AppVersionPolicy> findByPlatform(Platform platform);
    
    boolean existsByPlatform(Platform platform);
}
