package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.VerifyNicknameDuplicateServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record VerifyNicknameDuplicateRegisterRequest(
        @NotBlank(message = "닉네임을 입력해 주세요.")
        String nickName
) {

    public VerifyNicknameDuplicateServiceRequest toServiceRequest() {
        return new VerifyNicknameDuplicateServiceRequest(nickName);
    }
}
