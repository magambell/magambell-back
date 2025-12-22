package com.magambell.server.region.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.region.app.port.in.RegionUseCase;
import com.magambell.server.region.app.port.out.response.EupmyeondongListResponse;
import com.magambell.server.region.app.port.out.response.SidoListResponse;
import com.magambell.server.region.app.port.out.response.SigunguListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Region", description = "지역 정보 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/region")
@RestController
public class RegionController {

    private final RegionUseCase regionUseCase;

    @Operation(summary = "시·도 목록 조회", description = "전국 시·도 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = SidoListResponse.class))})
    @GetMapping("/city")
    public Response<SidoListResponse> getSidoList() {
        return new Response<>(regionUseCase.getSidoList());
    }

    @Operation(summary = "시·군·구 목록 조회", description = "특정 시·도에 속한 시·군·구 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = SigunguListResponse.class))})
    @GetMapping("/district")
    public Response<SigunguListResponse> getSigunguList(
            @Parameter(description = "시·도명", example = "서울특별시", required = true)
            @RequestParam String sido
    ) {
        return new Response<>(regionUseCase.getSigunguList(sido));
    }

    @Operation(summary = "읍·면·동 목록 조회", description = "특정 시·군·구에 속한 읍·면·동 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = EupmyeondongListResponse.class))})
    @GetMapping("/town")
    public Response<EupmyeondongListResponse> getEupmyeondongList(
            @Parameter(description = "시·도명", example = "서울특별시", required = true)
            @RequestParam String sido,
            @Parameter(description = "시·군·구명", example = "강남구", required = true)
            @RequestParam String sigungu
    ) {
        return new Response<>(regionUseCase.getEupmyeondongList(sido, sigungu));
    }
}
