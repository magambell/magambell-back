package com.magambell.server.appversion.app.port.out;

import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import java.util.List;
import java.util.Optional;

public interface AppVersionPolicyCommandPort {
    
    /**
     * 버전 정책 저장
     */
    AppVersionPolicy save(AppVersionPolicy policy);
    
    /**
     * 버전 정책 삭제
     */
    void delete(AppVersionPolicy policy);
    
    /**
     * ID로 조회
     */
    Optional<AppVersionPolicy> findById(Long policyId);
    
    /**
     * 모든 정책 조회
     */
    List<AppVersionPolicy> findAll();
    
    /**
     * 플랫폼별 존재 여부
     */
    boolean existsByPlatform(Platform platform);
}
