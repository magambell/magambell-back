package com.magambell.server.payment.app.service;

import com.magambell.server.order.domain.model.Order;
import com.magambell.server.payment.app.port.in.PaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentService implements PaymentUseCase {

    public static final String MERCHANT_UID_PREFIX = "ORDER_";
//    private final PortOnePort portOnePort;

    @Override
    public void createCheckout(final Order order) {

    }
}
