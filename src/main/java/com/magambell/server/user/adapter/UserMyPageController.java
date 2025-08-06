package com.magambell.server.user.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.user.adapter.in.web.UserEditRequest;
import com.magambell.server.user.adapter.out.persistence.MyPageStatsResponse;
import com.magambell.server.user.app.port.in.UserUseCase;
import com.magambell.server.user.app.port.out.dto.MyPageStatsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(summary = "마이페이지 이용횟수, 탄소, 절약한 금액")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = MyPageStatsResponse.class))})
    @GetMapping("")
    public Response<MyPageStatsResponse> getMyPage(
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        MyPageStatsDTO dto = userUseCase.getMyPage(customUserDetails.userId());
        return new Response<>(new MyPageStatsResponse(dto.purchaseCount(), dto.savedKg(), dto.savedPrice()));
    }
}
