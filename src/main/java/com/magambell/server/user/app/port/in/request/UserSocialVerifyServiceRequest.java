package com.magambell.server.user.app.port.in.request;

import com.magambell.server.auth.domain.ProviderType;

public record UserSocialVerifyServiceRequest(
        ProviderType providerType,
        String authCode
) {
}
