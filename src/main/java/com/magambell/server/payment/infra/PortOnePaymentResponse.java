package com.magambell.server.payment.infra;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.magambell.server.payment.domain.enums.EasyPayProvider;
import com.magambell.server.payment.domain.enums.PayType;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import java.time.LocalDateTime;

public record PortOnePaymentResponse(

        String id,

        @JsonProperty("merchant_uid")
        String merchantUid,

        @JsonProperty("transaction_id")
        String transactionId,

        PaymentStatus status,

        @JsonProperty("paid_at")
        LocalDateTime paidAt,

        @JsonProperty("pay_method")
        PayType payType,

        String cardName,

        @JsonProperty("easy_pay_provider")
        EasyPayProvider easyPayProvider,

        Amount amount
) {
    public record Amount(
            int total
    ) {
    }
}
