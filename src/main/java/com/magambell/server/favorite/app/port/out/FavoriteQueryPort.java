package com.magambell.server.favorite.app.port.out;

import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;

public interface FavoriteQueryPort {
    boolean existsUserIdAndStoreId(User user, Store store);
}
