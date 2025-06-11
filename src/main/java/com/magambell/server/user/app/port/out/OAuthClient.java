package com.magambell.server.user.app.port.out;

import com.magambell.server.user.app.dto.OAuthUserInfo;

public interface OAuthClient {
    OAuthUserInfo getUserInfo(String accessToken);
}
