package com.magambell.server.store.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Approved {
    APPROVED("승인"),
    REJECTED("거절"),
    WAITING("대기");

    private final String text;
}
