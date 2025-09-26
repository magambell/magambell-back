package com.magambell.server.payment.app.port.out;

import com.magambell.server.payment.domain.entity.Payment;

public interface PaymentQueryPort {
    Payment findByMerchantUidWithLockAndRelations(String merchantUid);
}
