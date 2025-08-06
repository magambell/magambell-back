package com.magambell.server.order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("결제 전(주문 생성)"),
    PAID("결제 완료"),
    ACCEPTED("매장(사장님) 픽업 승인"),
    REJECTED("매장(사장님) 픽업 거부"),
    COMPLETED("픽업 완료"),
    CANCELED("고객 취소"),
    FAILED("결제 실패");

    private final String text;
}
