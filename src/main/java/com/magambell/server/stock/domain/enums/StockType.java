package com.magambell.server.stock.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StockType {
    INIT("생성"),
    ORDER("주문"),
    CANCEL("취소"),
    MANUAL("수동 변경");

    private final String text;
}
