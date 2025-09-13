package com.magambell.server.user.domain.repository;

import com.magambell.server.user.domain.enums.UserStatus;
import com.magambell.server.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmailAndUserStatus(String email, UserStatus userStatus);

    Optional<User> findByEmailAndPasswordAndUserStatus(String email, String password, UserStatus userStatus);

    boolean existsByNickNameAndUserStatus(String nickName, UserStatus userStatus);

    Optional<User> findByIdAndUserStatus(Long userId, UserStatus userStatus);
}
