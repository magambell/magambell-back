package com.magambell.server.auth.domain.model;

public record JwtToken(String accessToken, String refreshToken) {
}
