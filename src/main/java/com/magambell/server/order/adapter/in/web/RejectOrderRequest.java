package com.magambell.server.order.adapter.in.web;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.order.app.port.in.request.RejectOrderServiceRequest;
import com.magambell.server.order.domain.enums.RejectReason;
import jakarta.validation.constraints.NotNull;

public record RejectOrderRequest(
        @NotNull(message = "주문 거절 사유를 선택해 주세요.")
        RejectReason rejectReason
) {
    public RejectOrderServiceRequest toService(final Long orderId, final Long userId) {
        if (rejectReason == RejectReason.SYSTEM) {
            throw new InvalidRequestException(ErrorCode.INVALID_CARD_NAME);
        }
        return new RejectOrderServiceRequest(orderId, userId, rejectReason);
    }
}
