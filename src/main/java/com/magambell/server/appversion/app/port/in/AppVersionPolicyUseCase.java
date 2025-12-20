package com.magambell.server.appversion.app.port.in;

import com.magambell.server.appversion.adapter.in.web.CreateAppVersionPolicyRequest;
import com.magambell.server.appversion.adapter.in.web.UpdateAppVersionPolicyRequest;
import com.magambell.server.appversion.app.port.in.dto.AppVersionPolicyResponse;
import com.magambell.server.appversion.app.port.in.dto.AppVersionPublicResponse;
import com.magambell.server.appversion.domain.enums.Platform;
import java.util.List;

public interface AppVersionPolicyUseCase {
    
    /**
     * 모든 버전 정책 조회
     */
    List<AppVersionPolicyResponse> getAllPolicies();
    
    /**
     * 플랫폼별 버전 정책 조회
     */
    AppVersionPolicyResponse getPolicyByPlatform(Platform platform);
    
    /**
     * 단건 조회
     */
    AppVersionPolicyResponse getPolicyById(Long policyId);
    
    /**
     * 버전 정책 생성
     */
    AppVersionPolicyResponse createPolicy(CreateAppVersionPolicyRequest request);
    
    /**
     * 버전 정책 수정
     */
    AppVersionPolicyResponse updatePolicy(Long policyId, UpdateAppVersionPolicyRequest request);
    
    /**
     * 버전 정책 활성화 (publish)
     */
    AppVersionPolicyResponse publishPolicy(Long policyId);
    
    /**
     * 버전 정책 비활성화 (unpublish)
     */
    AppVersionPolicyResponse unpublishPolicy(Long policyId);
    
    /**
     * 버전 정책 삭제
     */
    void deletePolicy(Long policyId);
    
    /**
     * 플랫폼별 최신 버전 정보 조회 (Public API용 - 최소 정보만 응답)
     */
    AppVersionPublicResponse getLatestVersionPublic(Platform platform);
}
