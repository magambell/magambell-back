package com.magambell.server.appversion.app.port.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import com.magambell.server.appversion.domain.enums.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "버전 정책 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppVersionPolicyResponse(
        @Schema(description = "정책 ID")
        Long policyId,
        
        @Schema(description = "플랫폼")
        Platform platform,
        
        @Schema(description = "최신 버전")
        String latestVersion,
        
        @Schema(description = "최소 지원 버전")
        String minSupportedVersion,
        
        @Schema(description = "권장 최소 버전")
        String recommendedMinVersion,
        
        @Schema(description = "강제 업데이트 여부")
        Boolean forceUpdate,
        
        @Schema(description = "Android Store URL")
        String androidStoreUrl,
        
        @Schema(description = "iOS Store URL")
        String iosStoreUrl,
        
        @Schema(description = "릴리스 노트")
        String releaseNotes,
        
        @Schema(description = "활성 상태")
        Boolean active,
        
        @Schema(description = "생성일시")
        LocalDateTime createdAt,
        
        @Schema(description = "수정일시")
        LocalDateTime modifiedAt
) {
    public static AppVersionPolicyResponse from(AppVersionPolicy policy) {
        return new AppVersionPolicyResponse(
                policy.getId(),
                policy.getPlatform(),
                policy.getLatestVersion(),
                policy.getMinSupportedVersion(),
                policy.getRecommendedMinVersion(),
                policy.getForceUpdate(),
                policy.getAndroidStoreUrl(),
                policy.getIosStoreUrl(),
                policy.getReleaseNotes(),
                policy.getActive(),
                policy.getCreatedAt(),
                policy.getModifiedAt()
        );
    }
}
