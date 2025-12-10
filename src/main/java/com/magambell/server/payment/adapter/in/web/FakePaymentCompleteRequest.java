package com.magambell.server.payment.adapter.in.web;

import com.magambell.server.payment.app.port.in.request.FakePaymentCompleteServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record FakePaymentCompleteRequest(
        @NotBlank(message = "merchantUid는 필수입니다.")
        String merchantUid
) {
    public FakePaymentCompleteServiceRequest toServiceRequest() {
        return new FakePaymentCompleteServiceRequest(merchantUid);
    }
}
