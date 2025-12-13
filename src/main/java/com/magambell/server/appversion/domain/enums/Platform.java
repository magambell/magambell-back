package com.magambell.server.appversion.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {
    ANDROID("Android"),
    IOS("iOS");

    private final String description;
}
