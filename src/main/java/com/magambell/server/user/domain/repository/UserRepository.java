package com.magambell.server.user.domain.repository;

import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findByIdAndUserRole(Long id, UserRole userRole);

    boolean existsByNickName(String nickName);
}
