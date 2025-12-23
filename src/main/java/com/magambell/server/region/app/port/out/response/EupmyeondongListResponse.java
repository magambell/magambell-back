package com.magambell.server.region.app.port.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "읍·면·동 목록 응답")
public record EupmyeondongListResponse(
        @Schema(description = "읍·면·동 목록", example = "[\"개포동\", \"논현동\", \"대치동\"]")
        List<String> townList
) {
    public static EupmyeondongListResponse of(List<String> townList) {
        return new EupmyeondongListResponse(townList);
    }
}
