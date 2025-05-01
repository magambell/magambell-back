package com.magambell.server.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerificationStatus {

    REGISTER("회원가입", "인증번호 [%s]를 입력해주세요."),
    USERNAME("아이디 찾기", "인증번호 [%s]를 입력해주세요."),
    PWD("비밀번호 찾기", "인증번호 [%s]를 입력해주세요."),
    EDIT("회원 정보 수정", "인증번호 [%s]를 입력해주세요.");

    private final String text;
    private final String verificationMessage;
}
