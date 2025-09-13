package com.magambell.server.user.domain.repository;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.app.port.out.dto.MyPageStatsDTO;
import com.magambell.server.user.app.port.out.dto.UserInfoDTO;
import com.magambell.server.user.domain.entity.User;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserBySocial(final ProviderType providerType, final String providerId);

    UserInfoDTO getUserInfo(Long userId);

    boolean existsUserBySocial(ProviderType providerType, String providerId);

    MyPageStatsDTO getMyPageData(Long userId);
}
