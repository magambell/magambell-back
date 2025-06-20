package com.magambell.server.payment.app.port.out;

import com.magambell.server.payment.domain.model.Payment;

public interface PaymentQueryPort {
    Payment findByMerchantUidJoinOrder(String merchantUid);
}
