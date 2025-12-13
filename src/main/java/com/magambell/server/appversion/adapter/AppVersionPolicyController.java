package com.magambell.server.appversion.adapter;

import com.magambell.server.appversion.adapter.in.web.CreateAppVersionPolicyRequest;
import com.magambell.server.appversion.adapter.in.web.UpdateAppVersionPolicyRequest;
import com.magambell.server.appversion.app.port.in.AppVersionPolicyUseCase;
import com.magambell.server.appversion.app.port.in.dto.AppVersionPolicyResponse;
import com.magambell.server.appversion.domain.enums.Platform;
import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin - App Version Policy", description = "관리자 전용 앱 버전 정책 관리 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/app-version-policies")
@RestController
public class AppVersionPolicyController {

    private final AppVersionPolicyUseCase appVersionPolicyUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 목록 조회", description = "모든 버전 정책 목록을 조회합니다")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public Response<List<AppVersionPolicyResponse>> getAllPolicies() {
        return new Response<>(appVersionPolicyUseCase.getAllPolicies());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "플랫폼별 버전 정책 조회", description = "특정 플랫폼의 버전 정책을 조회합니다")
    @ApiResponse(responseCode = "200")
    @GetMapping("/platform/{platform}")
    public Response<AppVersionPolicyResponse> getPolicyByPlatform(
            @PathVariable Platform platform
    ) {
        return new Response<>(appVersionPolicyUseCase.getPolicyByPlatform(platform));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 단건 조회", description = "ID로 버전 정책을 조회합니다")
    @ApiResponse(responseCode = "200")
    @GetMapping("/{policyId}")
    public Response<AppVersionPolicyResponse> getPolicyById(
            @PathVariable Long policyId
    ) {
        return new Response<>(appVersionPolicyUseCase.getPolicyById(policyId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 생성", description = "새로운 버전 정책을 생성합니다")
    @ApiResponse(responseCode = "200")
    @PostMapping
    public Response<AppVersionPolicyResponse> createPolicy(
            @RequestBody @Validated CreateAppVersionPolicyRequest request
    ) {
        return new Response<>(appVersionPolicyUseCase.createPolicy(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 수정", description = "기존 버전 정책을 수정합니다")
    @ApiResponse(responseCode = "200")
    @PatchMapping("/{policyId}")
    public Response<AppVersionPolicyResponse> updatePolicy(
            @PathVariable Long policyId,
            @RequestBody @Validated UpdateAppVersionPolicyRequest request
    ) {
        return new Response<>(appVersionPolicyUseCase.updatePolicy(policyId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 활성화 (Publish)", description = "버전 정책을 활성화하여 적용합니다")
    @ApiResponse(responseCode = "200")
    @PostMapping("/{policyId}/publish")
    public Response<AppVersionPolicyResponse> publishPolicy(
            @PathVariable Long policyId
    ) {
        return new Response<>(appVersionPolicyUseCase.publishPolicy(policyId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 비활성화 (Unpublish)", description = "버전 정책을 비활성화합니다 (긴급 롤백용)")
    @ApiResponse(responseCode = "200")
    @PostMapping("/{policyId}/unpublish")
    public Response<AppVersionPolicyResponse> unpublishPolicy(
            @PathVariable Long policyId
    ) {
        return new Response<>(appVersionPolicyUseCase.unpublishPolicy(policyId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "버전 정책 삭제", description = "버전 정책을 삭제합니다 (비활성화 권장)")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @DeleteMapping("/{policyId}")
    public Response<BaseResponse> deletePolicy(
            @PathVariable Long policyId
    ) {
        appVersionPolicyUseCase.deletePolicy(policyId);
        return new Response<>();
    }
}
