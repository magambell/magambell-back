package com.magambell.server.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    CUSTOMER("고객님"),
    OWNER("사장님"),
    ADMIN("관리자");

    private final String text;


}
