package com.magambell.server.auth.adapter.out.persistence;

import com.magambell.server.auth.app.port.in.dto.RefreshTokenDTO;
import com.magambell.server.auth.app.port.out.RefreshTokenQueryPort;
import com.magambell.server.auth.domain.model.RefreshToken;
import com.magambell.server.auth.domain.repository.RefreshTokenRepository;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class RefreshTokenQueryAdapter implements RefreshTokenQueryPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void deleteRefreshToken(final Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public Long saveRefreshToken(final RefreshTokenDTO refreshTokenDTO) {
        return refreshTokenRepository.save(RefreshToken.create(refreshTokenDTO))
                .getId();
    }

    @Override
    public RefreshToken findByUserId(final Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
