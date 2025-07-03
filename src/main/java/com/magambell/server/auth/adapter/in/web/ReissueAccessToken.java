package com.magambell.server.auth.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record ReissueAccessToken(
        @NotBlank(message = "refreshToken 값이 누락되었습니다.")
        String refreshToken
) {
}
