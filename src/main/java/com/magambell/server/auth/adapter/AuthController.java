package com.magambell.server.auth.adapter;

import com.magambell.server.auth.adapter.in.web.SocialLoginRequest;
import com.magambell.server.auth.app.port.in.AuthUseCase;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthUseCase authUseCase;

    @Operation(summary = "oAuth 회원가입 및 로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/oauth/login")
    public Response<BaseResponse> oauthLogin(@RequestBody @Validated final SocialLoginRequest request,
                                             HttpServletResponse response) {
        JwtToken jwtToken = authUseCase.loginOrSignUp(request.toServiceRequest());

        response.setHeader("Authorization", jwtToken.accessToken());
        response.setHeader("RefreshToken", jwtToken.refreshToken());
        return new Response<>();
    }
}
