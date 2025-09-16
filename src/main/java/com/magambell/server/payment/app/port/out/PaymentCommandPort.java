package com.magambell.server.payment.app.port.out;

import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.domain.entity.Payment;

public interface PaymentCommandPort {
    Payment createReadyPayment(CreatePaymentDTO createPaymentDTO);
}
