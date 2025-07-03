package com.magambell.server.favorite.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.favorite.app.port.out.FavoriteQueryPort;
import com.magambell.server.favorite.domain.repository.FavoriteRepository;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class FavoriteQueryAdapter implements FavoriteQueryPort {
    private final FavoriteRepository favoriteRepository;

    @Override
    public boolean existsUserIdAndStoreId(final User user, final Store store) {
        return favoriteRepository.existsByUserIdAndStoreId(user.getId(), store.getId());
    }
}
