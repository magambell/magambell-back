package com.magambell.server.favorite.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.favorite.app.port.out.FavoriteQueryPort;
import com.magambell.server.favorite.domain.model.Favorite;
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

    @Override
    public Favorite findByUserAndStore(final User user, final Store store) {
        return favoriteRepository.findByUserIdAndStoreId(user.getId(), store.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.FAVORITE_NOT_FOUND));
    }
}
