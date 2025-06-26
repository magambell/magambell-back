package com.magambell.server.review.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceSatisfaction {
    BAD("아쉬워요"),
    AVERAGE("적당해요"),
    GOOD("좋아요"),
    EXCELLENT("최고예요");

    private final String text;
}
