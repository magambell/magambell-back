package com.magambell.server.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("승인"),
    WAITING("대기"),
    REJECTED("거절"),
    WITHDRAWN("탈퇴");

    private final String text;
}
