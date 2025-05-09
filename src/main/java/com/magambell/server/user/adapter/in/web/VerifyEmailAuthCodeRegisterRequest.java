package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.VerifyEmailAuthCodeServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailAuthCodeRegisterRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        String email,

        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode
) {

    public VerifyEmailAuthCodeServiceRequest toServiceRequest() {
        return new VerifyEmailAuthCodeServiceRequest(email, authCode);
    }
}
