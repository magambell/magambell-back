package com.magambell.server.appversion.app.port.out;

import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import java.util.Optional;

public interface AppVersionPolicyQueryPort {
    
    /**
     * 플랫폼별 버전 정책 조회
     */
    Optional<AppVersionPolicy> findByPlatform(Platform platform);
}
