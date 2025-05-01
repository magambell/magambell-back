package com.magambell.server.user.adapter;

import com.magambell.server.common.ApiResponse;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.UserRegisterRequest;
import com.magambell.server.user.app.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserAuthController {

    private final UserUseCase userUseCase;

    @PostMapping("/signup")
    public ApiResponse<BaseResponse> register(@RequestBody @Validated UserRegisterRequest request) {
        userUseCase.register(request.toServiceRequest());
        return new ApiResponse<>();
    }
}
