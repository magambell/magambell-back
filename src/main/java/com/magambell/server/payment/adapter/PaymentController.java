package com.magambell.server.payment.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.payment.adapter.in.web.PaymentRedirectPaidRequest;
import com.magambell.server.payment.adapter.in.web.PortOneWebhookRequest;
import com.magambell.server.payment.app.port.in.PaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Payment", description = "Payment API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    @Operation(summary = "portone 리다이렉트 방식 - 결제완료")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/complete")
    public Response<BaseResponse> redirectComplete(
            @RequestBody @Validated final PaymentRedirectPaidRequest request) {
        log.info("========================================");
        log.info("PortOne Redirect 결제 완료 수신 - paymentId: {}", request.paymentId());
        log.info("========================================");
        
        paymentUseCase.redirectPaid(request.toServiceRequest());
        return new Response<>();
    }

    @Operation(summary = "portone 웹훅 방식 - 결제완료")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class))})
    @PostMapping("/webhook")
    public Response<BaseResponse> webhook(
            @RequestBody @Validated final PortOneWebhookRequest request) {
        log.info("========================================");
        log.info("PortOne Webhook 수신 - type: {}, timestamp: {}", request.type(), request.timestamp());
        log.info("Request Body: {}", request);
        log.info("========================================");
        
        if (request.type().startsWith("Transaction.")) {
            paymentUseCase.webhook(request.toServiceRequest());
        }
        return new Response<>();
    }
}
