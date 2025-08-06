package com.magambell.server.user.app.port.out;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import java.util.Optional;

public interface OAuthClient {
    ProviderType getProviderType();

    OAuthUserInfo getUserInfo(String accessToken);

    void userWithdraw(String accessToken);

    Optional<OAuthUserInfo> findUserBySocialId(String accessToken);
}
