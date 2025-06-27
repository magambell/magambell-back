package com.magambell.server.user.infra.oauth.apple;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.out.OAuthClient;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class AppleOauthClient implements OAuthClient {

    private final WebClient webClient;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.APPLE;
    }

    @Override
    public OAuthUserInfo getUserInfo(final String accessToken) {
        JWTClaimsSet claims = parseIdToken(accessToken);
        return extractUserInfo(claims);
    }

    @Override
    public void userWithdraw(final String accessToken) {

    }

    @Override
    public Optional<OAuthUserInfo> findUserBySocialId(final String accessToken) {
        try {
            JWTClaimsSet claims = parseIdToken(accessToken);
            return Optional.of(extractUserInfo(claims));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private JWTClaimsSet parseIdToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new NotFoundException(ErrorCode.OAUTH_APPLE_USER_NOT_FOUND);
        }
    }

    private OAuthUserInfo extractUserInfo(JWTClaimsSet claims) {
        try {
            String sub = claims.getSubject();
            String email = claims.getStringClaim("email");

            if (sub == null || email == null) {
                throw new NotFoundException(ErrorCode.OAUTH_APPLE_USER_NOT_FOUND);
            }

            return new OAuthUserInfo(
                    sub,
                    email,
                    ProviderType.APPLE
            );
        } catch (ParseException e) {
            throw new NotFoundException(ErrorCode.OAUTH_APPLE_USER_NOT_FOUND);
        }
    }
}
