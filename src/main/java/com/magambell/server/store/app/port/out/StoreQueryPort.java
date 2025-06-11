package com.magambell.server.store.app.port.out;

import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import java.util.Optional;

public interface StoreQueryPort {
    boolean existsByUser(User user);

    Optional<Store> getStoreByUser(User user);
}
