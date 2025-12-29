package com.magambell.server.region.app.port.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "읍·면·동 목록 응답")
public record EupmyeondongListResponse(
        @Schema(description = "읍·면·동 목록")
        List<TownDTO> townList
) {
    public static EupmyeondongListResponse of(List<TownDTO> townList) {
        return new EupmyeondongListResponse(townList);
    }

    @Schema(description = "읍·면·동 정보")
    public record TownDTO(
            @Schema(description = "지역 ID", example = "12345")
            Long regionId,
            @Schema(description = "읍·면·동명", example = "개포동")
            String name
    ) {
    }
}
