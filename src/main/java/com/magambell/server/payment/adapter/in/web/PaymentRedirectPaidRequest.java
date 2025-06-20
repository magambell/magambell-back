package com.magambell.server.payment.adapter.in.web;

import com.magambell.server.payment.app.port.in.request.PaymentRedirectPaidServiceRequest;

public record PaymentRedirectPaidRequest(String paymentId) {
    public PaymentRedirectPaidServiceRequest toServiceRequest() {
        return new PaymentRedirectPaidServiceRequest(paymentId);
    }
}
