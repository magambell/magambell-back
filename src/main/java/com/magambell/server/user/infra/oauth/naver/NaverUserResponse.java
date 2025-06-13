package com.magambell.server.user.infra.oauth.naver;

// 네이버 유저정보 응답 DTO
public record NaverUserResponse(
        Response response
) {
    public record Response(
            String id,
            String email,
            String mobile
    ) {
    }
}
