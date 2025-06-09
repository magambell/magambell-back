package com.magambell.server.store.app.port.out;

import com.magambell.server.user.domain.model.User;

public interface StoreQueryPort {
    boolean existsByUser(User user);
}
