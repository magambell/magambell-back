package com.magambell.server.payment.adapter.in.web;

import com.magambell.server.payment.app.port.in.request.PortOneWebhookServiceRequest;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;

public record PortOneWebhookRequest(
        @NotBlank
        String type,
        String timestamp,
        Data data
) {
    private record Data(
            String storeId,
            String paymentId,
            String transactionId,
            String cancellationId
    ) {
    }

    public PortOneWebhookServiceRequest toServiceRequest() {
        String paymentStatus = type.replace("Transaction.", "");
        return new PortOneWebhookServiceRequest(PaymentStatus.valueOf(paymentStatus), timestamp, data.storeId,
                data.paymentId, data.transactionId,
                data.cancellationId);
    }
}