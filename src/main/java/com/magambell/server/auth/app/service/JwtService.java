package com.magambell.server.auth.app.service;

import static com.magambell.server.auth.app.service.JwtTokenProvider.ACCESS_PREFIX_STRING;

import com.magambell.server.auth.domain.model.JwtToken;
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
        return UserRole.valueOf(jwt.getBody().get("userRole", String.class));
    }

    public boolean isValidJwtToken(final String token) {
        String tokenWithoutBearer = getTokenWithoutBearer(token);
        Jws<Claims> jwt = getJwt(tokenWithoutBearer);

        return jwt.getBody().getSubject() != null;
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
