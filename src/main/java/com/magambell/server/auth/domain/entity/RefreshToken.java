package com.magambell.server.auth.domain.entity;


import com.magambell.server.auth.app.port.in.dto.RefreshTokenDTO;
import com.magambell.server.common.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "refresh_token_id")
    private Long id;

    private String refreshToken;

    private Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    public RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public static RefreshToken create(final RefreshTokenDTO dto) {
        return RefreshToken.builder()
                .refreshToken(dto.refreshToken())
                .userId(dto.userId())
                .build();
    }
}
