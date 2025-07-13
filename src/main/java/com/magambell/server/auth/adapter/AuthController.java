package com.magambell.server.auth.adapter;

import com.magambell.server.auth.adapter.in.web.ReissueAccessToken;
import com.magambell.server.auth.adapter.in.web.SocialLoginRequest;
import com.magambell.server.auth.adapter.in.web.SocialWithdrawRequest;
import com.magambell.server.auth.app.port.in.AuthUseCase;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Operation(summary = "회원 탈퇴")
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @DeleteMapping("/withdraw")
    public Response<BaseResponse> withdraw(@RequestBody @Validated final SocialWithdrawRequest request,
                                           @AuthenticationPrincipal final CustomUserDetails customUserDetails) {
        authUseCase.withdrawUser(request.toService(), customUserDetails);
        return new Response<>();
    }

    @Operation(summary = "고객 토큰 재발행")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("/token/reissue")
    public Response<BaseResponse> reissueAccessToken(
            @RequestBody @Validated ReissueAccessToken reissueAccessToken,
            HttpServletResponse res) {
        JwtToken jwtToken = authUseCase.reissueAccessToken(reissueAccessToken.refreshToken());
        res.setHeader("Authorization", jwtToken.accessToken());
        res.setHeader("RefreshToken", jwtToken.refreshToken());
        return new Response<>();
    }

    @Operation(summary = "oAuth 테스트 고객님 계정 로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/user/test")
    public Response<BaseResponse> userTest(HttpServletResponse response) {
        //todo 애플, 구글 테스트 후 삭제
        JwtToken jwtToken = authUseCase.userTest();

        response.setHeader("Authorization", jwtToken.accessToken());
        response.setHeader("RefreshToken", jwtToken.refreshToken());
        return new Response<>();
    }

    @Operation(summary = "oAuth 테스트 사장님 계정 로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/owner/test")
    public Response<BaseResponse> ownerTest(HttpServletResponse response) {
        //todo 애플, 구글 테스트 후 삭제
        JwtToken jwtToken = authUseCase.ownerTest();

        response.setHeader("Authorization", jwtToken.accessToken());
        response.setHeader("RefreshToken", jwtToken.refreshToken());
        return new Response<>();
    }
}
