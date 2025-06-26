package com.magambell.server.review.app.port.out;

import com.magambell.server.order.domain.model.Order;
import com.magambell.server.user.domain.model.User;

public interface ReviewQueryPort {
    boolean existsOrderAndReview(Order order, User user);
}
