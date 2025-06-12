package com.magambell.server.auth.app.service;

import com.magambell.server.auth.app.port.in.dto.RefreshTokenDTO;
import com.magambell.server.auth.app.port.out.RefreshTokenQueryPort;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.TokenExpiredException;
import com.magambell.server.user.domain.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String ACCESS_PREFIX_STRING = "Bearer ";
    public static final String REFRESH_PREFIX_STRING = "RefreshToken ";
    public static final String CLAIMS_USER_ROLE = "UserRole";
    private static final Long VALIDITY_TIME = 1000L;

    private final SecretKey secretKey;
    private final Long accessTokenValiditySeconds;
    private final Long refreshTokenValiditySeconds;
    private final RefreshTokenQueryPort refreshTokenQueryPort;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.access-expire-time}") Long accessTokenValiditySeconds,
                            @Value("${jwt.refresh-expire-time}") Long refreshTokenValiditySeconds,
                            RefreshTokenQueryPort refreshTokenQueryPort
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySeconds = accessTokenValiditySeconds * VALIDITY_TIME;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds * VALIDITY_TIME;
        this.refreshTokenQueryPort = refreshTokenQueryPort;
    }

    public JwtToken createJwtToken(Long userId, UserRole userRole) {
        String accessToken = createAccessToken(String.valueOf(userId), userRole);
        String refreshToken = createRefreshToken(userId, userRole);
        return new JwtToken(accessToken, refreshToken);
    }

    public Jws<Claims> getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException(ErrorCode.JWT_VERIFY_EXPIRED);
        } catch (JwtException ex) {
            throw new TokenExpiredException(ErrorCode.JWT_VALIDATE_ERROR);
        }
    }

    private String createAccessToken(String subject, UserRole userRole) {
        return ACCESS_PREFIX_STRING + createToken(accessTokenValiditySeconds, subject, userRole);
    }

    private String createRefreshToken(Long userId, UserRole userRole) {
        String token =
                REFRESH_PREFIX_STRING + createToken(refreshTokenValiditySeconds, String.valueOf(userId), userRole);
        deleteRefreshToken(userId);
        saveRefreshToken(userId, token);
        return token;
    }

    private String createToken(Long validityTime, String subject, UserRole userRole) {
        Date now = new Date();
        Date validTime = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(CLAIMS_USER_ROLE, userRole)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validTime)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private void deleteRefreshToken(final Long userId) {
        refreshTokenQueryPort.deleteRefreshToken(userId);
    }

    private void saveRefreshToken(final Long userId, final String refreshToken) {
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO(refreshToken, userId);
        refreshTokenQueryPort.saveRefreshToken(refreshTokenDTO);
    }
}
