package com.magambell.server.user.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.VerifyEmailSignupRequest;
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
    @PostMapping("/email/signup")
    public Response<BaseResponse> emailSignupDuplicate(@RequestBody @Validated final VerifyEmailSignupRequest request) {
        userVerifyUseCase.emailSignupDuplicate(request.toServiceRequest());
        return new Response<>();
    }
}
