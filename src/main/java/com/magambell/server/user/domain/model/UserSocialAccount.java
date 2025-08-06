package com.magambell.server.user.domain.model;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserSocialAccount extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "user_social_account_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder(access = AccessLevel.PRIVATE)
    private UserSocialAccount(final ProviderType providerType, final String providerId) {
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public static UserSocialAccount create(final UserSocialAccountDTO dto) {
        return UserSocialAccount.builder()
                .providerType(dto.providerType())
                .providerId(dto.providerId())
                .build();
    }

    public void addUser(final User user) {
        this.user = user;
    }
}
