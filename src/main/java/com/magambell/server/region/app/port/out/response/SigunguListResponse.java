package com.magambell.server.region.app.port.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "시·군·구 목록 응답")
public record SigunguListResponse(
        @Schema(description = "시·군·구 목록", example = "[\"강남구\", \"강동구\", \"강북구\"]")
        List<String> sigunguList
) {
    public static SigunguListResponse of(List<String> sigunguList) {
        return new SigunguListResponse(sigunguList);
    }
}
