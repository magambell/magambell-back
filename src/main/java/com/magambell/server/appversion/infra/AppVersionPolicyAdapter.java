package com.magambell.server.appversion.infra;

import com.magambell.server.appversion.app.port.out.AppVersionPolicyCommandPort;
import com.magambell.server.appversion.app.port.out.AppVersionPolicyQueryPort;
import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import com.magambell.server.appversion.domain.repository.AppVersionPolicyRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppVersionPolicyAdapter implements AppVersionPolicyQueryPort, AppVersionPolicyCommandPort {

    private final AppVersionPolicyRepository appVersionPolicyRepository;

    @Override
    public Optional<AppVersionPolicy> findByPlatform(Platform platform) {
        return appVersionPolicyRepository.findByPlatform(platform);
    }

    @Override
    public AppVersionPolicy save(AppVersionPolicy policy) {
        return appVersionPolicyRepository.save(policy);
    }

    @Override
    public void delete(AppVersionPolicy policy) {
        appVersionPolicyRepository.delete(policy);
    }

    @Override
    public Optional<AppVersionPolicy> findById(Long policyId) {
        return appVersionPolicyRepository.findById(policyId);
    }

    @Override
    public List<AppVersionPolicy> findAll() {
        return appVersionPolicyRepository.findAll();
    }

    @Override
    public boolean existsByPlatform(Platform platform) {
        return appVersionPolicyRepository.existsByPlatform(platform);
    }
}
