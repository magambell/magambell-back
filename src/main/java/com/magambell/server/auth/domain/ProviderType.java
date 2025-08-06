package com.magambell.server.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플"),
    NAVER("네이버");

    private final String text;
}
