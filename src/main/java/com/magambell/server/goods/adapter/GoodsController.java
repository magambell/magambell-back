package com.magambell.server.goods.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.goods.adapter.in.web.ChangeGoodsStatusRequest;
import com.magambell.server.goods.adapter.in.web.RegisterGoodsRequest;
import com.magambell.server.goods.app.port.in.GoodsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Goods", description = "Goods API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/goods")
@RestController
public class GoodsController {

    private final GoodsUseCase goodsUseCase;

    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "마감백 등록")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("")
    public Response<BaseResponse> registerStore(
            @RequestBody @Validated final RegisterGoodsRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        goodsUseCase.registerGoods(request.toService(), customUserDetails.userId());
        return new Response<>();
    }

    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "마감백 판매 여부 변경")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BaseResponse.class))})
    @PatchMapping("/status")
    public Response<BaseResponse> changeGoodsStatus(
            @RequestBody @Validated final ChangeGoodsStatusRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        goodsUseCase.changeGoodsStatus(request.toService(customUserDetails.userId()), LocalDate.now());
        return new Response<>();
    }
}
