package com.magambell.server.stock.app.port.in;

import com.magambell.server.payment.domain.model.Payment;

public interface StockUseCase {
    void restoreStockIfNecessary(Payment payment);
}
