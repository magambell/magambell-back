package com.magambell.server.auth.app.port.in.request;

import com.magambell.server.auth.domain.ProviderType;

public record SocialWithdrawServiceRequest(
        ProviderType providerType,
        String authCode
) {
}
