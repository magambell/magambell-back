package com.magambell.server.auth.app.port.out;

import com.magambell.server.auth.app.port.in.dto.RefreshTokenDTO;
import com.magambell.server.auth.domain.model.RefreshToken;

public interface RefreshTokenQueryPort {
    void deleteRefreshToken(Long userId);

    Long saveRefreshToken(RefreshTokenDTO refreshTokenDTO);

    RefreshToken findByUserId(Long userId);
}
