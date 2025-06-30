package com.magambell.server.user.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.UserLoginRequest;
import com.magambell.server.user.adapter.in.web.UserRegisterRequest;
import com.magambell.server.user.adapter.out.persistence.UserInfoResponse;
import com.magambell.server.user.app.port.in.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Auth", description = "User Auth API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserAuthController {

    private final UserUseCase userUseCase;

    @Operation(summary = "일반 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/register")
    public Response<BaseResponse> register(@RequestBody @Validated final UserRegisterRequest request) {
        userUseCase.register(request.toServiceRequest());
        return new Response<>();
    }

    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/login")
    public Response<BaseResponse> login(@RequestBody @Validated final UserLoginRequest request) {
        userUseCase.login(request.toServiceRequest());
        return new Response<>();
    }

    @Operation(summary = "유저 정보 조회")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserInfoResponse.class))})
    @GetMapping("/me")
    public Response<UserInfoResponse> getUserInfo(@AuthenticationPrincipal final CustomUserDetails customUserDetails) {
        return new Response<>(userUseCase.getUserInfo(customUserDetails));
    }
}
