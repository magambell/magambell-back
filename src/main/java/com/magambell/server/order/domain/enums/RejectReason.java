package com.magambell.server.order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RejectReason {
    COOKING_DELAY("조리 지연"),
    OUT_OF_STOCK("재료 소진"),
    UNDELIVERABLE_AREA("배달 불가 지역"),
    STORE_ISSUE("가게 사정");

    private final String text;
}
