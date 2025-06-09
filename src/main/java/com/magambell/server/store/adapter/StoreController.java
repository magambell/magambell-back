package com.magambell.server.store.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.store.adapter.in.web.RegisterStoreRequest;
import com.magambell.server.store.app.port.in.StoreUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
    @Operation(summary = "매장등록")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("")
    public Response<BaseResponse> registerStore(
            @RequestBody @Validated final RegisterStoreRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        storeUseCase.registerStore(request.toServiceRequest(), customUserDetails.userId());
        return new Response<>();
    }
}
