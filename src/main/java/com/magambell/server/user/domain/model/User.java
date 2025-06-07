package com.magambell.server.user.domain.model;

import com.magambell.server.common.BaseTimeEntity;
import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.enums.UserRole;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSocialAccount> userSocialAccounts = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private User(final String email, final String password, final String name, final String nickName,
                 final String phoneNumber,
                 final UserRole userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public void addUserSocialAccount(final UserSocialAccount userSocialAccount) {
        this.userSocialAccounts.add(userSocialAccount);
        userSocialAccount.addUser(this);
    }

    public static User create(final UserDTO dto) {
        return User.builder()
                .email(dto.email())
                .password(dto.password())
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .userRole(dto.userRole())
                .build();
    }

    public static User createBySocial(final UserSocialAccountDTO dto) {
        return User.builder()
                .email(dto.email())
                .name(dto.name())
                .nickName(dto.nickName())
                .phoneNumber(dto.phoneNumber())
                .userRole(dto.userRole())
                .build();
    }
}
