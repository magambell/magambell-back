package com.magambell.server.goods.app.scheduler;

import com.magambell.server.goods.app.port.in.GoodsUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoodsStatusScheduler {

    private final GoodsUseCase goodsUseCase;

    @Scheduled(cron = "0 */5 * * * *")
    public void changeSaleStatusToOff() {
        goodsUseCase.changeSaleStatusToOff(LocalDateTime.now());
    }
}
