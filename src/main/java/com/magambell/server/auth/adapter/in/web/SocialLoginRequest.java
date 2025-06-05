package com.magambell.server.auth.adapter.in.web;

import com.magambell.server.auth.app.port.in.request.SocialLoginServiceRequest;
import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.domain.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialLoginRequest(
        @NotNull(message = "잘못된 형식 입니다.")
        ProviderType providerType,

        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode,

        @NotNull(message = "사용자 유형을 선택해 주세요.")
        UserRole userRole
) {
    public SocialLoginServiceRequest toServiceRequest() {
        return new SocialLoginServiceRequest(providerType, authCode, userRole);
    }
}
