package com.magambell.server.user.adapter.out.persistence;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.domain.enums.UserRole;

public record UserInfoResponse(
        String email,
        String nickName,
        UserRole userRole,
        ProviderType providerType,
        Approved approved,
        Long goodsId
) {
}
