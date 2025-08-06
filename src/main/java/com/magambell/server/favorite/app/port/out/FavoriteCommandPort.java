package com.magambell.server.favorite.app.port.out;

import com.magambell.server.favorite.domain.model.Favorite;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;

public interface FavoriteCommandPort {
    void registerFavorite(Store store, User user);

    void deleteFavorite(Favorite favorite);
}
