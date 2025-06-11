package com.magambell.server.user.app.dto;

import com.magambell.server.auth.domain.ProviderType;

public record OAuthUserInfo(
        String id,
        String email,
        String name,
        String phoneNumber,
        ProviderType providerType
) {
}
