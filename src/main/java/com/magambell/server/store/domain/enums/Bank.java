package com.magambell.server.store.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bank {
    농협("농협은행"),
    신한("신한은행"),
    카카오뱅크("카카오뱅크"),
    KB국민("KB국민은행"),
    케이뱅크("케이뱅크"),
    토스뱅크("토스뱅크"),
    IBK기업("IBK 기업은행"),
    우리("우리은행"),
    하나("하나은행"),
    KDB산업("KDB 산업은행"),
    신협("신협은행"),
    부산("부산은행"),
    수협("수협은행"),
    제주("제주은행"),
    대구("대구은행"),
    새마을("새마을은행"),
    전북("전북은행"),
    SC제일("SC 제일은행"),
    광주("광주은행"),
    경남("경남은행"),
    SBI저축("SBI 저축은행"),
    상호저축("상호저축은행"),
    KEB외환("KEB 외환은행"),
    산림조합("산림조합은행"),
    KB증권("KB 증권"),
    NH투자증권("NH 투자증권"),
    한국투자증권("한국투자증권"),
    ;

    private final String text;

}
