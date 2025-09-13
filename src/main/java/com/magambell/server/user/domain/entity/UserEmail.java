package com.magambell.server.user.domain.entity;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.domain.enums.VerificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserEmail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_email_id")
    private Long id;

    private String email;

    private String authCode;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private UserEmail(final String email, final String authCode, final VerificationStatus verificationStatus) {
        this.email = email;
        this.authCode = authCode;
        this.verificationStatus = verificationStatus;
    }

    public static UserEmail create(UserEmailDTO dto) {
        return UserEmail.builder()
                .email(dto.email())
                .authCode(dto.authCode())
                .verificationStatus(dto.verificationStatus())
                .build();
    }
}
