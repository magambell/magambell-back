package com.magambell.server.goods.domain.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaleStatus {
    ON("판매중"),
    OFF("판매종료");

    private final String description;
}
