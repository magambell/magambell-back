package com.magambell.server.user.app.port.out;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.adapter.out.persistence.UserInfoResponse;
import com.magambell.server.user.domain.enums.UserRole;

public record UserInfoDTO(
        String email,
        UserRole userRole,
        ProviderType providerType,
        Approved approved
) {
    public UserInfoResponse toResponse() {
        return new UserInfoResponse(email, userRole, providerType, approved);
    }
}
