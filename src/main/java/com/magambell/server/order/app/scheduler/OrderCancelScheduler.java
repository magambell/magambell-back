package com.magambell.server.order.app.scheduler;

import com.magambell.server.order.app.port.in.OrderUseCase;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderCancelScheduler {

    private static final int PICKUP_AUTO_REJECT_MINUTES = 30;
    private static final int AUTO_REJECT_AFTER_ORDER_MINUTES = 5;

    private final OrderUseCase orderUseCase;

    @Scheduled(cron = "0 * * * * *")
    public void batchRejectOrdersBeforePickup() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime pickupTime = now.plusMinutes(PICKUP_AUTO_REJECT_MINUTES);
        LocalDateTime createdAtCutOff = pickupTime.minusMinutes(
                PICKUP_AUTO_REJECT_MINUTES + AUTO_REJECT_AFTER_ORDER_MINUTES);
        orderUseCase.batchRejectOrdersBeforePickup(pickupTime, createdAtCutOff);
    }

    @Scheduled(cron = "0 * * * * *")
    public void autoRejectOrdersAfter() {
        // todo 5분 마다 거절 로직 추가해야함
//        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//        LocalDateTime pickupTime = now.plusMinutes(PICKUP_AUTO_REJECT_MINUTES);
//        LocalDateTime createdAtCutOff = pickupTime.minusMinutes(
//                PICKUP_AUTO_REJECT_MINUTES + AUTO_REJECT_AFTER_ORDER_MINUTES);
//        LocalDateTime minusMinutes = now.minusMinutes(AUTO_REJECT_AFTER_ORDER_MINUTES);
//
//        orderUseCase.autoRejectOrdersAfter(minusMinutes, pickupTime, createdAtCutOff);
    }
}
