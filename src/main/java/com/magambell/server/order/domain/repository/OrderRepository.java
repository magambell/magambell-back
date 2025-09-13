package com.magambell.server.order.domain.repository;

import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.user.domain.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> user(User user);
}
