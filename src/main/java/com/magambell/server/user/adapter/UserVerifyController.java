package com.magambell.server.user.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.UserSocialVerifyRequest;
import com.magambell.server.user.adapter.in.web.VerifyEmailAuthCodeRegisterRequest;
import com.magambell.server.user.adapter.in.web.VerifyEmailDuplicateRegisterRequest;
import com.magambell.server.user.adapter.in.web.VerifyEmailSendRegisterRequest;
import com.magambell.server.user.app.port.in.UserVerifyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Verify", description = "Verify API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
@RestController
public class UserVerifyController {

    private final UserVerifyUseCase userVerifyUseCase;

    @Operation(summary = "회원가입시 이메일 중복 검사")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/email/register")
    public Response<BaseResponse> emailRegisterDuplicate(
            @RequestBody @Validated final VerifyEmailDuplicateRegisterRequest request) {
        userVerifyUseCase.emailRegisterDuplicate(request.toServiceRequest());
        return new Response<>();
    }

    @Operation(summary = "회원가입시 이메일 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/email/register/send")
    public Response<BaseResponse> emailRegisterSend(
            @RequestBody @Validated final VerifyEmailSendRegisterRequest request) {
        userVerifyUseCase.emailRegisterSend(request.toServiceRequest());
        return new Response<>();
    }

    @Operation(summary = "회원가입시 이메일 인증번호 검증")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/email/register/authCode")
    public Response<BaseResponse> emailRegisterAuthCodeCheck(
            @RequestBody @Validated final VerifyEmailAuthCodeRegisterRequest request) {
        userVerifyUseCase.emailRegisterAuthCodeCheck(request.toServiceRequest());
        return new Response<>();
    }

    @Operation(summary = "기존 유저인지 아닌지 검증")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Boolean.class))})
    @PostMapping("/social")
    public Response<Boolean> verifySocialUser(@RequestBody @Validated final UserSocialVerifyRequest request) {
        return new Response<>(userVerifyUseCase.verifySocialUser(request.toServiceRequest()));
    }
}
