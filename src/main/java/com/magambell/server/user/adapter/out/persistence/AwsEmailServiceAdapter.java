package com.magambell.server.user.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.user.app.port.in.AwsEmailServiceInputPort;
import com.magambell.server.user.domain.enums.VerificationStatus;
import com.magambell.server.user.infra.aws.SimpleEmailService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class AwsEmailServiceAdapter implements AwsEmailServiceInputPort {

    private final SimpleEmailService simpleEmailService;

    @Override
    public void sendEmail(final String to, final String authCode, final VerificationStatus verificationStatus) {
        simpleEmailService.emailSend(getSubject(), String.format(verificationStatus.getVerificationMessage(), authCode),
                to);
    }

    private String getSubject() {
        return "마감벨 입니다. 이메일 인증번호를 확인해 주세요.";
    }
}
