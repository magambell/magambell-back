package com.magambell.server.stock.app.port.in;

import com.magambell.server.payment.domain.entity.Payment;

public interface StockUseCase {
    void restoreStockIfNecessary(Payment payment);
}
