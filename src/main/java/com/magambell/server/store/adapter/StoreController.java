package com.magambell.server.store.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.store.adapter.in.web.RegisterStoreRequest;
import com.magambell.server.store.adapter.in.web.SearchStoreListRequest;
import com.magambell.server.store.adapter.in.web.StoreApproveRequest;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.StoreUseCase;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Store", description = "Store API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
@RestController
public class StoreController {

    private final StoreUseCase storeUseCase;

    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "매장 등록")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = StoreImagesResponse.class))})
    @PostMapping("")
    public Response<StoreImagesResponse> registerStore(
            @RequestBody @Validated final RegisterStoreRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        return new Response<>(storeUseCase.registerStore(request.toServiceRequest(), customUserDetails.userId()));
    }

    @Operation(summary = "매장 리스트")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = StoreListResponse.class))})
    @GetMapping("")
    public Response<StoreListResponse> getStoreList(
            @ModelAttribute @Validated final SearchStoreListRequest request
    ) {
        return new Response<>(storeUseCase.getStoreList(request.toService()));
    }

    @Operation(summary = "매장 리스트")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = StoreDetailResponse.class))})
    @GetMapping("/{storeId}")
    public Response<StoreDetailResponse> getStore(
            @PathVariable final Long storeId
    ) {
        StoreDetailDTO storeDetail = storeUseCase.getStoreDetail(storeId);
        return new Response<>(storeDetail.toResponse());
    }

    @Operation(summary = "매장 승인")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/approve")
    public Response<BaseResponse> storeApprove(
            @RequestBody @Validated final StoreApproveRequest request
    ) {
        //todo 배포시 관리자만 가능하게 변경 예정
        storeUseCase.storeApprove(request.toService());
        return new Response<>();
    }
}
