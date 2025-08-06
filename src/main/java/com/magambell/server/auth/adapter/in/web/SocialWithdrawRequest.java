package com.magambell.server.auth.adapter.in.web;

import com.magambell.server.auth.app.port.in.request.SocialWithdrawServiceRequest;
import com.magambell.server.auth.domain.ProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SocialWithdrawRequest(
        @NotNull(message = "잘못된 형식 입니다.")
        ProviderType providerType,

        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode
) {
    public SocialWithdrawServiceRequest toService() {
        return new SocialWithdrawServiceRequest(providerType, authCode);
    }
}
