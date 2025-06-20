package com.magambell.server.payment.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.payment.app.port.out.PaymentQueryPort;
import com.magambell.server.payment.domain.model.Payment;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class PaymentQueryAdapter implements PaymentQueryPort {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment findByMerchantUidJoinOrder(final String merchantUid) {
        return paymentRepository.findByMerchantUidJoinOrder(merchantUid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
    }
}
