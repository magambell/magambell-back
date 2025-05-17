package com.magambell.server.user.domain.repository;

import com.magambell.server.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String username);

    Optional<User> findByEmailAndPassword(String email, String password);
}
