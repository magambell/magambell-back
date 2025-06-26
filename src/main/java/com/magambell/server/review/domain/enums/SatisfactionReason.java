package com.magambell.server.review.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SatisfactionReason {
    VARIETY("다양한 구성의 마감백"),
    AFFORDABLE("저렴한 가격"),
    FRIENDLY("친절한 사장님"),
    ZERO("Zero Food Waste 기여");

    private final String text;
}
