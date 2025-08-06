package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.VerifyEmailSendServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailSendRegisterRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        String email
) {

    public VerifyEmailSendServiceRequest toServiceRequest() {
        return new VerifyEmailSendServiceRequest(email);
    }
}
