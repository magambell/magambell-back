package com.magambell.server.region.app.port.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "시·도 목록 응답")
public record SidoListResponse(
        @Schema(description = "시·도 목록")
        List<CityDTO> cityList
) {
    public static SidoListResponse of(List<CityDTO> cityList) {
        return new SidoListResponse(cityList);
    }

    @Schema(description = "시·도 정보")
    public record CityDTO(
            @Schema(description = "지역 ID (시도 전체)", example = "1100000000")
            Long regionId,
            @Schema(description = "시·도명", example = "서울")
            String name
    ) {
    }
}
