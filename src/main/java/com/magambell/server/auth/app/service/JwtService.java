package com.magambell.server.auth.app.service;

import static com.magambell.server.auth.app.service.JwtTokenProvider.ACCESS_PREFIX_STRING;
import static com.magambell.server.auth.app.service.JwtTokenProvider.CLAIMS_USER_ROLE;

import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.auth.domain.model.RefreshToken;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.TokenExpiredException;
import com.magambell.server.user.domain.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken createJwtToken(Long userId, UserRole userRole) {
        return jwtTokenProvider.createJwtToken(userId, userRole);
    }

    public Long getJwtUserId(final String token) {
        String tokenWithoutBearer = getTokenWithoutBearer(token);
        Jws<Claims> jwt = getJwt(tokenWithoutBearer);
        return Long.valueOf(jwt.getBody().getSubject());
    }

    public UserRole getJwtUserRole(final String token) {
        String tokenWithoutBearer = getTokenWithoutBearer(token);
        Jws<Claims> jwt = getJwt(tokenWithoutBearer);
        return UserRole.valueOf(jwt.getBody().get(CLAIMS_USER_ROLE, String.class));
    }

    public boolean isValidJwtToken(final String token) {
        String tokenWithoutBearer = getTokenWithoutBearer(token);
        Jws<Claims> jwt = getJwt(tokenWithoutBearer);

        return jwt.getBody().getSubject() != null;
    }

    @Transactional
    public JwtToken reissueAccessToken(String token) {
        Long userId = getJwtUserId(token);
        UserRole userRole = getJwtUserRole(token);

        RefreshToken refreshToken = jwtTokenProvider.getRefreshTokenByUserId(userId);

        if (!refreshToken.getRefreshToken().equals(token)) {
            throw new TokenExpiredException(ErrorCode.JWT_VALIDATE_ERROR);
        }

        if (!refreshToken.getUserId().equals(userId)) {
            throw new TokenExpiredException(ErrorCode.JWT_VALIDATE_ERROR);
        }

        return jwtTokenProvider.createJwtToken(refreshToken.getUserId(), userRole);
    }

    private Jws<Claims> getJwt(final String token) {
        return jwtTokenProvider.getTokenClaims(token);
    }

    private String getTokenWithoutBearer(final String token) {
        return Arrays.stream(token.split(ACCESS_PREFIX_STRING))
                .filter(s -> !s.trim().isEmpty())
                .findFirst()
                .orElse("");
    }
}
