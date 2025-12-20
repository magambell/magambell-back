package com.magambell.server.appversion.app.port.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.magambell.server.appversion.domain.entity.AppVersionPolicy;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공개용 앱 버전 정보 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppVersionPublicResponse(
        @Schema(description = "최소 지원 버전", example = "1.0.0")
        String minSupportedVersion,
        
        @Schema(description = "릴리스 노트")
        String releaseNotes
) {
    public static AppVersionPublicResponse from(AppVersionPolicy policy) {
        return new AppVersionPublicResponse(
                policy.getMinSupportedVersion(),
                policy.getReleaseNotes()
        );
    }
}
