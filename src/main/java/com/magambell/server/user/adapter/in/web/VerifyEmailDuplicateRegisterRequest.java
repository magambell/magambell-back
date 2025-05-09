package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.VerifyEmailDuplicateServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailDuplicateRegisterRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        String email
) {

    public VerifyEmailDuplicateServiceRequest toServiceRequest() {
        return new VerifyEmailDuplicateServiceRequest(email);
    }
}
