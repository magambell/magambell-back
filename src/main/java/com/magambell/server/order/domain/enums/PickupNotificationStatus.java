package com.magambell.server.order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PickupNotificationStatus {
    NOT_SENT("미발송"),
    SENT("발송 완료"),
    FAILED("발송 실패");

    private final String text;
}