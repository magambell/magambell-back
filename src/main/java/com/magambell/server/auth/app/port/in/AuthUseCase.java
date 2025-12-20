package com.magambell.server.auth.app.port.in;

import com.magambell.server.auth.app.port.in.request.SocialLoginServiceRequest;
import com.magambell.server.auth.app.port.in.request.SocialWithdrawServiceRequest;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.common.security.CustomUserDetails;

public interface AuthUseCase {
    JwtToken loginOrSignUp(SocialLoginServiceRequest request);

    void withdrawUser(SocialWithdrawServiceRequest request, CustomUserDetails customUserDetails);

    JwtToken reissueAccessToken(String refreshToken);

    JwtToken userTest();

    JwtToken ownerTest();

    JwtToken adminTest();
}
