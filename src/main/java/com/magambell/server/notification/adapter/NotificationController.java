package com.magambell.server.notification.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.notification.adapter.in.web.SaveFcmTokenRequest;
import com.magambell.server.notification.adapter.in.web.SaveStoreOpenFcmTokenRequest;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "Notification API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@RestController
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @Operation(summary = "매장 오픈 FCM 토큰 등록")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/store")
    public Response<BaseResponse> saveStoreOpenToken(
            @RequestBody @Validated SaveStoreOpenFcmTokenRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        notificationUseCase.saveStoreOpenToken(request.toService(customUserDetails.userId()));
        return new Response<>();
    }

    @Operation(summary = "FCM 토큰 등록")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("")
    public Response<BaseResponse> saveToken(
            @RequestBody @Validated SaveFcmTokenRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        notificationUseCase.saveToken(request.toService(customUserDetails.userId()));
        return new Response<>();
    }
}
