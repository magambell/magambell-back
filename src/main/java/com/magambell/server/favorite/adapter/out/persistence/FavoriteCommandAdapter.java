package com.magambell.server.favorite.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.favorite.app.port.out.FavoriteCommandPort;
import com.magambell.server.favorite.domain.model.Favorite;
import com.magambell.server.favorite.domain.repository.FavoriteRepository;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class FavoriteCommandAdapter implements FavoriteCommandPort {
    private final FavoriteRepository favoriteRepository;

    @Override
    public void registerFavorite(final Store store, final User user) {
        favoriteRepository.save(Favorite.create(store, user));
    }
}
