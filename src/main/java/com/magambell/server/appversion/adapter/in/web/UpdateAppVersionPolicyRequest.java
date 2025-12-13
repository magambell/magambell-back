package com.magambell.server.appversion.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "버전 정책 수정 요청")
public record UpdateAppVersionPolicyRequest(
        @Schema(description = "최신 버전", example = "1.2.0")
        String latestVersion,
        
        @Schema(description = "최소 지원 버전", example = "1.0.0")
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
