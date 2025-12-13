package com.magambell.server.appversion.app.port.in.dto;

import com.magambell.server.appversion.domain.enums.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "버전 정책 생성 요청")
public record CreateAppVersionPolicyRequest(
        @Schema(description = "플랫폼", example = "ANDROID")
        @NotNull(message = "플랫폼은 필수입니다")
        Platform platform,
        
        @Schema(description = "최신 버전", example = "1.2.0")
        @NotBlank(message = "최신 버전은 필수입니다")
        String latestVersion,
        
        @Schema(description = "최소 지원 버전", example = "1.0.0")
        @NotBlank(message = "최소 지원 버전은 필수입니다")
        String minSupportedVersion,
        
        @Schema(description = "권장 최소 버전", example = "1.1.0")
        String recommendedMinVersion,
        
        @Schema(description = "강제 업데이트 여부", example = "false")
        Boolean forceUpdate,
        
        @Schema(description = "Android Play Store URL")
        String androidStoreUrl,
        
        @Schema(description = "iOS App Store URL")
        String iosStoreUrl,
        
        @Schema(description = "릴리스 노트 (선택)")
        String releaseNotes
) {
}
