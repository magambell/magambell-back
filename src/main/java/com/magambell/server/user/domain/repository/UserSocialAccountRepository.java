package com.magambell.server.user.domain.repository;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.domain.model.UserSocialAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccount, Long> {
    Optional<UserSocialAccount> findByProviderTypeAndProviderId(ProviderType providerType, String id);
}
