package com.magambell.server.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL("중복된 이메일 입니다."),
    DUPLICATE_STORE("매장이 이미 존재합니다."),
    DUPLICATE_NICKNAME("중복된 닉네임 입니다."),

    USER_VALID_PASSWORD("비밀번호는 8~16자리의 영문 대문자와 소문자, 특수문자 숫자의 조합이어야 합니다."),
    USER_VALID_EMAIL("유효한 이메일 주소가 아닙니다."),
    USER_VALID_PHONE("휴대폰 번호는 하이픈이 없어야 하며, 10자리 또는 11자리 숫자여야 합니다."),
    USER_ROLE_NOT_ASSIGNABLE("해당 역할은 사용자에게 할당할 수 없습니다."),
    STORE_VALID_IMAGE("이미지를 등록해 주세요."),
    INVALID_ONLY_NUMBER("숫자만 입력해 주세요."),
    TIME_VALID("판매 시작 시간은 종료 시간보다 빨라야합니다."),
    INVALID_NICK_NAME("닉네임을 입력해 주세요."),
    INVALID_USER_ROLE("사용자 유형을 선택해 주세요."),
    INVALID_PHONE_NUMBER("휴대폰 번호를 입력해 주세요."),

    USER_CUSTOMER_NO_ACCESS("사장님만 접근되는 메뉴입니다."),

    USER_EMAIL_AUTH_CODE_NOT_EQUALS("인증번호가 일치하지 않습니다."),

    USER_EMAIL_NOT_FOUND("인증을 하지 않았습니다."),
    USER_NOT_FOUND("계정이 존재하지 않았습니다."),
    OAUTH_KAKAO_USER_NOT_FOUND("카카오 사용자 정보를 찾을 수 없습니다."),
    BANK_NOT_FOUND("은행정보를 찾을 수 없습니다."),
    OWNER_NOT_FOUND_STORE("매장 정보를 찾을 수 없습니다."),

    JWT_VERIFY_EXPIRED("인증정보가 만료 됐습니다."),
    JWT_VALIDATE_ERROR("유효한 인증정보가 아닙니다."),
    ;
    private final String message;
}
