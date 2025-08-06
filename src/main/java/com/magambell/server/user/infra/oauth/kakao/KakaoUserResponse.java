package com.magambell.server.user.infra.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserResponse(
        Long id,

        @JsonProperty("connected_at")
        String connectedAt,

        Properties properties,

        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record Properties(
            String nickname
    ) {
    }

    public record KakaoAccount(
            String email
    ) {
    }
}
