package com.magambell.server.favorite.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.favorite.app.port.in.FavoriteUseCase;
import com.magambell.server.favorite.app.port.out.FavoriteCommandPort;
import com.magambell.server.favorite.app.port.out.FavoriteQueryPort;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FavoriteService implements FavoriteUseCase {

    private final FavoriteCommandPort favoriteCommandPort;
    private final FavoriteQueryPort favoriteQueryPort;
    private final UserQueryPort userQueryPort;
    private final StoreQueryPort storeQueryPort;

    @Transactional
    @Override
    public void registerFavorite(final Long storeId, final Long userId) {
        User user = userQueryPort.findById(userId);
        Store store = storeQueryPort.findById(storeId);

        if (favoriteQueryPort.existsUserIdAndStoreId(user, store)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_FAVORITE);
        }

        favoriteCommandPort.registerFavorite(store, user);
    }
}
