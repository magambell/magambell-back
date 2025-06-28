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

        @JsonProperty("card")
        Card card,

        @JsonProperty("easy_pay")
        EasyPay easyPay,

        Amount amount
) {
    public record Card(
            @JsonProperty("company")
            String company
    ) {
    }

    public record EasyPay(
            @JsonProperty("provider")
            EasyPayProvider provider
    ) {
    }

    public record Amount(
            int total
    ) {
    }
}
