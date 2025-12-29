package com.magambell.server.region.app.port.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "시·군·구 목록 응답")
public record SigunguListResponse(
        @Schema(description = "시·군·구 목록")
        List<DistrictDTO> districtList
) {
    public static SigunguListResponse of(List<DistrictDTO> districtList) {
        return new SigunguListResponse(districtList);
    }

    @Schema(description = "시·군·구 정보")
    public record DistrictDTO(
            @Schema(description = "지역 ID (시군구 전체)", example = "1111000000")
            Long regionId,
            @Schema(description = "시·군·구명", example = "강남구")
            String name
    ) {
    }
}
