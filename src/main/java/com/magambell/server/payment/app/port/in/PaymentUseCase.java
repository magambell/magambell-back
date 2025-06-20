package com.magambell.server.payment.app.port.in;

import com.magambell.server.payment.app.port.in.request.PaymentRedirectPaidServiceRequest;
import com.magambell.server.payment.app.port.in.request.PortOneWebhookServiceRequest;

public interface PaymentUseCase {

    void redirectPaid(PaymentRedirectPaidServiceRequest request);

    void webhook(PortOneWebhookServiceRequest request);
}
