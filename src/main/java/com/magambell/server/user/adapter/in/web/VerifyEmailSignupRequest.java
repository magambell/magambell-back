package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailSignupRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        String email
) {

    public VerifyEmailServiceRequest toServiceRequest() {
        return new VerifyEmailServiceRequest(email);
    }
}
