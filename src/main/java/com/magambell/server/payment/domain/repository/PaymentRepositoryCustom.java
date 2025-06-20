package com.magambell.server.payment.domain.repository;

import com.magambell.server.payment.domain.model.Payment;
import java.util.Optional;

public interface PaymentRepositoryCustom {
    Optional<Payment> findByMerchantUidJoinOrder(String merchantUid);
}
