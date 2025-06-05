package com.magambell.server.auth.app.port.in;

import com.magambell.server.auth.app.port.in.request.SocialLoginServiceRequest;
import com.magambell.server.auth.domain.model.JwtToken;

public interface AuthUseCase {
    JwtToken loginOrSignUp(SocialLoginServiceRequest request);
}
