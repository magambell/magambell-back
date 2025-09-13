package com.magambell.server.favorite.app.port.out;

import com.magambell.server.favorite.domain.entity.Favorite;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;

public interface FavoriteCommandPort {
    void registerFavorite(Store store, User user);

    void deleteFavorite(Favorite favorite);
}
