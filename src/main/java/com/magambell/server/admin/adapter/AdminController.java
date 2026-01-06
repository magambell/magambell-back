package com.magambell.server.admin.adapter;

import com.magambell.server.admin.adapter.in.web.AdminEditStoreRequest;
import com.magambell.server.admin.app.port.in.AdminUseCase;
import com.magambell.server.admin.app.port.out.response.AdminStatsResponse;
import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Admin API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    private final AdminUseCase adminUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "통계 조회 - 가입 유저수(일반 사용자), 가입 매장수(사장님)")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = AdminStatsResponse.class))})
    @GetMapping("/stats")
    public Response<AdminStatsResponse> getStats() {
        return new Response<>(adminUseCase.getStats());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "매장 정보 수정 - Store 정보, Goods 정보, 이미지 모두 수정 가능")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BaseResponse.class))})
    @PutMapping("/stores/{storeId}")
    public Response<BaseResponse> editStore(
            @PathVariable Long storeId,
            @RequestBody @Validated AdminEditStoreRequest request
    ) {
        return new Response<>(adminUseCase.editStore(storeId, request.toServiceRequest()));
    }
}
