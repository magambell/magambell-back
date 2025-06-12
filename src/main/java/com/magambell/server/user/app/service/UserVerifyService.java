package com.magambell.server.user.app.service;

import static com.magambell.server.user.domain.enums.VerificationStatus.REGISTER;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.NotEqualException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.common.utility.GenerateUtility;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.in.AwsEmailServiceInputPort;
import com.magambell.server.user.app.port.in.UserVerifyUseCase;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.app.port.in.request.UserSocialVerifyServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailAuthCodeServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailDuplicateServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailSendServiceRequest;
import com.magambell.server.user.app.port.out.OAuthClient;
import com.magambell.server.user.app.port.out.UserEmailQueryPort;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.enums.VerificationStatus;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserVerifyService implements UserVerifyUseCase {

    private final UserQueryPort userQueryPort;
    private final UserEmailQueryPort userEmailQueryPort;
    private final AwsEmailServiceInputPort awsEmailServiceInputPort;
    private final Map<ProviderType, OAuthClient> oAuthClientMap;

    public UserVerifyService(final UserQueryPort userQueryPort, final UserEmailQueryPort userEmailQueryPort,
                             final AwsEmailServiceInputPort awsEmailServiceInputPort,
                             final List<OAuthClient> oAuthClients) {
        this.userQueryPort = userQueryPort;
        this.userEmailQueryPort = userEmailQueryPort;
        this.awsEmailServiceInputPort = awsEmailServiceInputPort;
        this.oAuthClientMap = oAuthClients.stream()
                .collect(Collectors.toMap(OAuthClient::getProviderType, Function.identity()));
    }

    @Override
    public void emailRegisterDuplicate(final VerifyEmailDuplicateServiceRequest request) {
        if (userQueryPort.existsByEmail(request.email())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Override
    public void emailRegisterAuthCodeCheck(final VerifyEmailAuthCodeServiceRequest request) {
        validateEmailAndAuthCode(request.email(), request.authCode(), REGISTER);
    }

    @Override
    public void emailRegisterSend(final VerifyEmailSendServiceRequest serviceRequest) {
        String authCode = GenerateUtility.getAuthCode();

        userEmailQueryPort.deleteEmail(serviceRequest.email());
        userEmailQueryPort.saveUserEmail(serviceRequest.email(), authCode, REGISTER);

        awsEmailServiceInputPort.sendEmail(serviceRequest.email(), authCode, REGISTER);
    }

    @Override
    public boolean verifySocialUser(final UserSocialVerifyServiceRequest request) {
        OAuthClient oAuthClient = oAuthClientMap.get(request.providerType());
        OAuthUserInfo userInfo = oAuthClient.getUserInfo(request.authCode());
        return userQueryPort.existsUserBySocial(userInfo.providerType(), userInfo.id());
    }

    private void validateEmailAndAuthCode(final String email, final String authCode,
                                          final VerificationStatus verificationStatus) {
        UserEmailDTO userEmailDTO = userEmailQueryPort.findRegisterUserEmail(email, verificationStatus)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_EMAIL_NOT_FOUND));

        if (!userEmailDTO.authCode().equals(authCode)) {
            throw new NotEqualException(ErrorCode.USER_EMAIL_AUTH_CODE_NOT_EQUALS);
        }
    }

}
