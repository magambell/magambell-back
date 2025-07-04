package com.magambell.server.user.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.UserEditRequest;
import com.magambell.server.user.app.port.in.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MyPage Auth", description = "MyPage API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
@RestController
public class UserMyPageController {

    private final UserUseCase userUseCase;

    @Operation(summary = "회원 닉네임 수정")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PatchMapping("/nickname")
    public Response<BaseResponse> userEdit(
            @RequestBody @Validated final UserEditRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        userUseCase.userEdit(request.toServiceRequest(customUserDetails.userId()));
        return new Response<>();
    }
}
