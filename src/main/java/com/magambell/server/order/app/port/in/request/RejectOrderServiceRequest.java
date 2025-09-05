package com.magambell.server.order.app.port.in.request;

import com.magambell.server.order.domain.enums.RejectReason;

public record RejectOrderServiceRequest(Long orderId, Long userId, RejectReason rejectReason) {
}
