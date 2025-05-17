package com.magambell.server.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL("중복된 이메일입니다."),

    USER_VALID_PASSWORD("비밀번호는 8~16자리의 영문 대문자와 소문자, 특수문자 숫자의 조합이어야 합니다."),
    USER_VALID_EMAIL("유효한 이메일 주소가 아닙니다."),
    USER_VALID_PHONE("휴대폰 번호는 하이픈이 없어야 하며, 10자리 또는 11자리 숫자여야 합니다."),

    USER_EMAIL_AUTH_CODE_NOT_EQUALS("인증번호가 일치하지 않습니다."),

    USER_EMAIL_NOT_FOUND("인증을 하지 않았습니다."),
    USER_NOT_FOUND("계정이 존재하지 않았습니다."),
    ;
    private final String message;
}
