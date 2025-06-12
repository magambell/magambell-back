package com.magambell.server.user.adapter.in.web;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.app.port.in.request.UserSocialVerifyServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSocialVerifyRequest(
        @NotNull(message = "잘못된 형식 입니다.")
        ProviderType providerType,

        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode
) {
    public UserSocialVerifyServiceRequest toServiceRequest() {
        return new UserSocialVerifyServiceRequest(providerType, authCode);
    }
}
