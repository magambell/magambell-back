package com.magambell.server.review.app.port.out.response;

import com.magambell.server.review.domain.enums.SatisfactionReason;

import java.time.LocalDateTime;
import java.util.Set;

public record ReviewReportListDTO(
        Long userId,
        String nickName
) {
}
