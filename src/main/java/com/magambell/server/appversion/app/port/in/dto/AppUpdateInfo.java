package com.magambell.server.appversion.app.port.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "앱 업데이트 정보")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppUpdateInfo(
        @Schema(description = "강제 업데이트 필요 여부", example = "true")
        Boolean forceUpdate,
        
        @Schema(description = "업데이트 권장 여부", example = "false")
        Boolean recommended,
        
        @Schema(description = "최신 버전", example = "1.2.0")
        String latestVersion,
        
        @Schema(description = "현재 버전", example = "1.0.0")
        String currentVersion,
        
        @Schema(description = "스토어 URL", example = "https://play.google.com/store/apps/details?id=com.magambell")
        String storeUrl,
        
        @Schema(description = "릴리스 노트 (선택)", example = "중요한 보안 업데이트가 포함되어 있습니다.")
        String releaseNotes
) {
    public static AppUpdateInfo forceUpdate(String latestVersion, String currentVersion, 
                                           String storeUrl, String releaseNotes) {
        return new AppUpdateInfo(true, false, latestVersion, currentVersion, storeUrl, releaseNotes);
    }
    
    public static AppUpdateInfo recommended(String latestVersion, String currentVersion, 
                                           String storeUrl, String releaseNotes) {
        return new AppUpdateInfo(false, true, latestVersion, currentVersion, storeUrl, releaseNotes);
    }
}
