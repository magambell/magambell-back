package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.LoginServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(

        @NotBlank(message = "이메일을 입력해 주세요.")
        String email,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password
) {
    public LoginServiceRequest toServiceRequest() {
        return new LoginServiceRequest(email, password);
    }
}
