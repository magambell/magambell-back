package com.magambell.server.region.app.port.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "시·도 목록 응답")
public record SidoListResponse(
        @Schema(description = "시·도 목록", example = "[\"서울특별시\", \"경기도\", \"부산광역시\"]")
        List<String> sidoList
) {
    public static SidoListResponse of(List<String> sidoList) {
        return new SidoListResponse(sidoList);
    }
}
