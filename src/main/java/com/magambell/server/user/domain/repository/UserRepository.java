package com.magambell.server.user.domain.repository;

import com.magambell.server.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String username);

}
