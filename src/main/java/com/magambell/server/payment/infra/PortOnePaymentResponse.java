package com.magambell.server.payment.infra;

import com.magambell.server.payment.domain.enums.PaymentStatus;
import java.time.OffsetDateTime;

public record PortOnePaymentResponse(

        String id,

        String transactionId,

        String merchantId,

        PaymentStatus status,

        OffsetDateTime paidAt,

        Method method,

        Amount amount
) {

    public record Method(
            String type,
            EasyPayMethod easyPayMethod,
            String provider
    ) {
    }

    public record EasyPayMethod(
            String type
    ) {
    }

    public record Amount(
            int total
    ) {
    }
}
