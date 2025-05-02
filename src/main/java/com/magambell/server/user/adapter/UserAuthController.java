package com.magambell.server.user.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.UserRegisterRequest;
import com.magambell.server.user.app.port.in.UserUseCase;
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

@Tag(name = "User Auth", description = "User Auth API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserAuthController {

    private final UserUseCase userUseCase;

    @Operation(summary = "일반 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/signup")
    public Response<BaseResponse> register(@RequestBody @Validated UserRegisterRequest request) {
        userUseCase.register(request.toServiceRequest());
        return new Response<>();
    }
}
