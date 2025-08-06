package com.magambell.server.user.app.port.out.dto;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.adapter.out.persistence.UserInfoResponse;
import com.magambell.server.user.domain.enums.UserRole;

public record UserInfoDTO(
        String email,
        String nickName,
        UserRole userRole,
        ProviderType providerType,
        Approved approved,
        Long goodsId
) {
    public UserInfoResponse toResponse() {
        return new UserInfoResponse(email, nickName, userRole, providerType, approved, String.valueOf(goodsId));
    }
}
