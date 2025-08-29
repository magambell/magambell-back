package com.magambell.server.payment.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentCompletionType {
    REDIRECT("리다이렉트"),
    WEBHOOK("웹훅");

    private final String text;
}
