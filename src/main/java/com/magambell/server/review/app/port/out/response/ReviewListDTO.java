package com.magambell.server.review.app.port.out.response;

import com.magambell.server.review.domain.enums.SatisfactionReason;
import com.magambell.server.review.domain.enums.ServiceSatisfaction;
import java.time.LocalDateTime;
import java.util.List;

public record ReviewListDTO(
        Long reviewId,
        ServiceSatisfaction serviceSatisfaction,
        SatisfactionReason satisfactionReason,
        String description,
        LocalDateTime createdAt,
        List<String> imageUrls,
        Long goodsId,
        Long storeId
) {
}
