package com.magambell.server.appversion.adapter;

import com.magambell.server.appversion.app.port.in.AppVersionPolicyUseCase;
import com.magambell.server.appversion.app.port.in.dto.AppVersionPublicResponse;
import com.magambell.server.appversion.domain.enums.Platform;
import com.magambell.server.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "App Version", description = "앱 버전 정보 조회 API (Public)")
@RequiredArgsConstructor
@RequestMapping("/api/v1/app-version")
@RestController
public class AppVersionController {

    private final AppVersionPolicyUseCase appVersionPolicyUseCase;

    @Operation(
        summary = "플랫폼별 최신 버전 정보 조회", 
        description = "특정 플랫폼의 최소 지원 버전과 릴리스 노트를 조회합니다 (인증 불필요)"
    )
    @ApiResponse(responseCode = "200")
    @GetMapping
    public Response<AppVersionPublicResponse> getLatestVersion(
            @RequestParam Platform platform
    ) {
        return new Response<>(appVersionPolicyUseCase.getLatestVersionPublic(platform));
    }
}
