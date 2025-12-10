package com.magambell.server.payment.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.payment.adapter.in.web.FakePaymentCompleteRequest;
import com.magambell.server.payment.app.port.in.PaymentCompleteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 결제 완료 API
 * - local, dev 환경에서만 활성화
 * - 실제 PG 없이 주문 상태를 PAID로 변경 가능
 */
@Slf4j
@Profile({"local", "dev"})
@Tag(name = "Test Payment", description = "테스트용 결제 API (local/dev 전용)")
@RequiredArgsConstructor
@RequestMapping("/api/test/payment")
@RestController
public class FakePaymentController {

    private final PaymentCompleteUseCase paymentCompleteUseCase;

    @Operation(
            summary = "[테스트용] 강제 결제 완료",
            description = "실제 PG 없이 주문 상태를 PENDING → PAID로 변경합니다. local/dev 환경에서만 사용 가능합니다."
    )
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/force-complete")
    public Response<BaseResponse> forceComplete(
            @RequestBody @Validated final FakePaymentCompleteRequest request) {
        
        log.info("[TEST] Force completing payment for merchantUid: {}", request.merchantUid());
        paymentCompleteUseCase.completePayment(request.merchantUid());
        
        return new Response<>();
    }
}
