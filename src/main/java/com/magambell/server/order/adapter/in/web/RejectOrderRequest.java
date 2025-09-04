package com.magambell.server.order.adapter.in.web;

import com.magambell.server.order.app.port.in.request.RejectOrderServiceRequest;
import com.magambell.server.order.domain.enums.RejectReason;
import jakarta.validation.constraints.NotNull;

public record RejectOrderRequest(
        @NotNull(message = "주문 거절 사유를 선택해 주세요.")
        RejectReason rejectReason
) {
    public RejectOrderServiceRequest toService(final Long orderId, final Long userId) {
        return new RejectOrderServiceRequest(orderId, userId, rejectReason);
    }
}
