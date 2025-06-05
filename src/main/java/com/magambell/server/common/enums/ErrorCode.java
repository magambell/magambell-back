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
    USER_ROLE_NOT_ASSIGNABLE("해당 역할은 사용자에게 할당할 수 없습니다."),

    USER_EMAIL_AUTH_CODE_NOT_EQUALS("인증번호가 일치하지 않습니다."),

    USER_EMAIL_NOT_FOUND("인증을 하지 않았습니다."),
    USER_NOT_FOUND("계정이 존재하지 않았습니다."),
    OAUTH_KAKAO_USER_NOT_FOUND("카카오 사용자 정보를 찾을 수 없습니다."),

    JWT_VERIFY_EXPIRED("인증정보가 만료 됐습니다."),
    JWT_VALIDATE_ERROR("유효한 인증정보가 아닙니다."),
    ;
    private final String message;
}
