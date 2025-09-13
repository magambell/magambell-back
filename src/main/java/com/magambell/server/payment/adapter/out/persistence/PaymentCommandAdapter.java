package com.magambell.server.payment.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.app.port.out.PaymentCommandPort;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class PaymentCommandAdapter implements PaymentCommandPort {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment createReadyPayment(final CreatePaymentDTO dto) {
        return paymentRepository.save(dto.toPayment());
    }
}
