package com.magambell.server.auth.app.port.in.request;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.domain.enums.UserRole;

public record SocialLoginServiceRequest(
        ProviderType providerType,
        String authCode,
        String nickName,
        String phoneNumber,
        UserRole userRole
) {
}
