package com.magambell.server.appversion.app.service;

import com.magambell.server.appversion.adapter.in.web.CreateAppVersionPolicyRequest;
import com.magambell.server.appversion.adapter.in.web.UpdateAppVersionPolicyRequest;
import com.magambell.server.appversion.app.port.in.AppVersionPolicyUseCase;
import com.magambell.server.appversion.app.port.in.dto.AppVersionPolicyResponse;
import com.magambell.server.appversion.app.port.in.dto.AppVersionPublicResponse;
import com.magambell.server.appversion.app.port.out.AppVersionPolicyCommandPort;
import com.magambell.server.appversion.app.port.out.AppVersionPolicyQueryPort;
import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AppVersionPolicyService implements AppVersionPolicyUseCase {

    private final AppVersionPolicyCommandPort appVersionPolicyCommandPort;
    private final AppVersionPolicyQueryPort appVersionPolicyQueryPort;

    @Override
    public List<AppVersionPolicyResponse> getAllPolicies() {
        return appVersionPolicyCommandPort.findAll().stream()
                .map(AppVersionPolicyResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public AppVersionPolicyResponse getPolicyByPlatform(Platform platform) {
        AppVersionPolicy policy = appVersionPolicyQueryPort.findByPlatform(platform)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드
        return AppVersionPolicyResponse.from(policy);
    }

    @Override
    public AppVersionPolicyResponse getPolicyById(Long policyId) {
        AppVersionPolicy policy = appVersionPolicyCommandPort.findById(policyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드
        return AppVersionPolicyResponse.from(policy);
    }

    @Transactional
    @Override
    public AppVersionPolicyResponse createPolicy(CreateAppVersionPolicyRequest request) {
        // 동일 플랫폼 정책이 이미 존재하는지 확인
        if (appVersionPolicyCommandPort.existsByPlatform(request.platform())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL); // TODO: 적절한 에러코드
        }

        AppVersionPolicy policy = AppVersionPolicy.create(
                request.platform(),
                request.latestVersion(),
                request.minSupportedVersion(),
                request.recommendedMinVersion(),
                request.forceUpdate(),
                request.androidStoreUrl(),
                request.iosStoreUrl(),
                request.releaseNotes()
        );

        AppVersionPolicy savedPolicy = appVersionPolicyCommandPort.save(policy);
        log.info("버전 정책 생성: policyId={}, platform={}", savedPolicy.getId(), savedPolicy.getPlatform());
        
        return AppVersionPolicyResponse.from(savedPolicy);
    }

    @Transactional
    @Override
    public AppVersionPolicyResponse updatePolicy(Long policyId, UpdateAppVersionPolicyRequest request) {
        AppVersionPolicy policy = appVersionPolicyCommandPort.findById(policyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드

        policy.update(
                request.latestVersion() != null ? request.latestVersion() : policy.getLatestVersion(),
                request.minSupportedVersion() != null ? request.minSupportedVersion() : policy.getMinSupportedVersion(),
                request.recommendedMinVersion(),
                request.forceUpdate(),
                request.androidStoreUrl(),
                request.iosStoreUrl(),
                request.releaseNotes()
        );

        AppVersionPolicy updatedPolicy = appVersionPolicyCommandPort.save(policy);
        log.info("버전 정책 수정: policyId={}, platform={}", updatedPolicy.getId(), updatedPolicy.getPlatform());
        
        return AppVersionPolicyResponse.from(updatedPolicy);
    }

    @Transactional
    @Override
    public AppVersionPolicyResponse publishPolicy(Long policyId) {
        AppVersionPolicy policy = appVersionPolicyCommandPort.findById(policyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드

        policy.setActive(true);
        AppVersionPolicy publishedPolicy = appVersionPolicyCommandPort.save(policy);
        
        log.info("버전 정책 활성화: policyId={}, platform={}", publishedPolicy.getId(), publishedPolicy.getPlatform());
        
        return AppVersionPolicyResponse.from(publishedPolicy);
    }

    @Transactional
    @Override
    public AppVersionPolicyResponse unpublishPolicy(Long policyId) {
        AppVersionPolicy policy = appVersionPolicyCommandPort.findById(policyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드

        policy.setActive(false);
        AppVersionPolicy unpublishedPolicy = appVersionPolicyCommandPort.save(policy);
        
        log.info("버전 정책 비활성화: policyId={}, platform={}", unpublishedPolicy.getId(), unpublishedPolicy.getPlatform());
        
        return AppVersionPolicyResponse.from(unpublishedPolicy);
    }

    @Transactional
    @Override
    public void deletePolicy(Long policyId) {
        AppVersionPolicy policy = appVersionPolicyCommandPort.findById(policyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드

        appVersionPolicyCommandPort.delete(policy);
        log.info("버전 정책 삭제: policyId={}, platform={}", policyId, policy.getPlatform());
    }

    @Override
    public AppVersionPublicResponse getLatestVersionPublic(Platform platform) {
        AppVersionPolicy policy = appVersionPolicyQueryPort.findByPlatform(platform)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)); // TODO: 적절한 에러코드
        return AppVersionPublicResponse.from(policy);
    }
}
