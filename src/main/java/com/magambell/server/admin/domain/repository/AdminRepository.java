package com.magambell.server.admin.domain.repository;

import com.magambell.server.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<User, Long>, AdminRepositoryCustom {
}
