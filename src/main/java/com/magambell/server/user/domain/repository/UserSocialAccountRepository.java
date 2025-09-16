package com.magambell.server.user.domain.repository;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.domain.entity.UserSocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccount, Long> {
    void deleteByProviderTypeAndUserId(ProviderType providerType, Long userId);
}
