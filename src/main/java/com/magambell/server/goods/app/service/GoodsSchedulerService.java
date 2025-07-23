package com.magambell.server.goods.app.service;

import com.magambell.server.goods.app.port.in.GoodsUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GoodsSchedulerService {

    private final GoodsUseCase goodsUseCase;

    @Transactional
    public void changeSaleStatusToOff(final LocalDateTime now) {
        goodsUseCase.changeSaleStatusToOff(now);
    }
}
