package com.magambell.server.payment.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    READY("결제 대기"),
    PAID("결제 완료"),
    CANCELLED("결제 취소"),
    FAILED("결제 실패");

    private final String text;

    @JsonCreator
    public static PaymentStatus from(String value) {
        return switch (value.toLowerCase()) {
            case "ready" -> READY;
            case "paid" -> PAID;
            case "cancelled" -> CANCELLED;
            case "failed" -> FAILED;
            default -> throw new InvalidRequestException(ErrorCode.INVALID_PAYMENT_STATUS);
        };
    }
}
