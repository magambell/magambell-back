package com.magambell.server.user.infra.oauth.naver;


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
