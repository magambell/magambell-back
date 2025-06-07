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
            String email,
            String name,
            @JsonProperty("phone_number")
            String phoneNumber
    ) {
        public KakaoAccount(final String email, final String name, final String phoneNumber) {
            this.email = email;
            this.name = name;
            this.phoneNumber = toDomesticFormat(phoneNumber);
        }

        private String toDomesticFormat(String phone) {
            String digits = phone.replaceAll("[^0-9]", "");
            if (digits.startsWith("82") && digits.length() > 10) {
                return "0" + digits.substring(2);
            }
            return digits;
        }
    }
}
