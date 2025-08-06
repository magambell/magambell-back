package com.magambell.server.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    CUSTOMER("고객님", true),
    OWNER("사장님", true),
    ADMIN("관리자", false);

    private final String text;
    private final boolean userAssignable;

    public boolean isUserAssignable() {
        return userAssignable;
    }
}
