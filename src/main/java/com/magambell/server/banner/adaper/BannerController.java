package com.magambell.server.banner.adaper;

import com.magambell.server.banner.adaper.in.web.RegisterBannerRequest;
import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.BannerUseCase;
import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.store.adapter.in.web.*;
import com.magambell.server.store.adapter.out.persistence.*;
import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Banner", description = "Banner API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/banner")
@RestController
public class BannerController {

    private final BannerUseCase bannerUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 등록")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BannerImagesResponse.class))})
    @PostMapping("")
    public Response<BannerImagesResponse> registerBanner(
            @RequestBody @Validated final RegisterBannerRequest request
    ) {
        return new Response<>(bannerUseCase.registerBanner(request.toServiceRequest()));
    }


}
