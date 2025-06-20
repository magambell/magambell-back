package com.magambell.server.payment.app.port.in.request;

import com.magambell.server.payment.domain.enums.PaymentStatus;

public record PortOneWebhookServiceRequest(
        PaymentStatus paymentStatus,
        String timestamp,
        String storeId,
        String paymentId,
        String transactionId,
        String cancellationId
) {
}
