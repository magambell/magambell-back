package com.magambell.server.review.app.port.out.response;

import com.magambell.server.review.domain.enums.SatisfactionReason;
import java.time.LocalDateTime;
import java.util.Set;

public record ReviewListDTO(
        Long reviewId,
        Integer rating,
        Set<SatisfactionReason> satisfactionReasons,
        String description,
        LocalDateTime createdAt,
        Set<String> imageUrls,
        String nickName,
        Long goodsId,
        Long storeId
) {
    public String getReviewId() {
        return String.valueOf(reviewId);
    }

    public String getGoodsId() {
        return String.valueOf(goodsId);
    }

    public String getStoreId() {
        return String.valueOf(storeId);
    }
}
