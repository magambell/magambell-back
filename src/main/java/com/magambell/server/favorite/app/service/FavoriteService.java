package com.magambell.server.favorite.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.favorite.app.port.in.FavoriteUseCase;
import com.magambell.server.favorite.app.port.in.request.FavoriteStoreListServiceRequest;
import com.magambell.server.favorite.app.port.out.FavoriteCommandPort;
import com.magambell.server.favorite.app.port.out.FavoriteQueryPort;
import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import com.magambell.server.favorite.domain.entity.Favorite;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

        if (existsUserAndStore(user, store)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_FAVORITE);
        }

        favoriteCommandPort.registerFavorite(store, user);
    }

    @Transactional
    @Override
    public void deleteFavorite(final Long storeId, final Long userId) {
        User user = userQueryPort.findById(userId);
        Store store = storeQueryPort.findById(storeId);
        Favorite favorite = favoriteQueryPort.findByUserAndStore(user, store);
        favoriteCommandPort.deleteFavorite(favorite);
    }

    @Override
    public List<FavoriteStoreListDTOResponse> getFavoriteStoreList(final FavoriteStoreListServiceRequest request) {
        User user = userQueryPort.findById(request.userId());
        return favoriteQueryPort.getFavoriteStoreList(PageRequest.of(request.page() - 1, request.size()), user);
    }

    @Override
    public boolean checkFavoriteStore(final Long storeId, final Long userId) {
        User user = userQueryPort.findById(userId);
        Store store = storeQueryPort.findById(storeId);
        return existsUserAndStore(user, store);
    }

    private boolean existsUserAndStore(final User user, final Store store) {
        return favoriteQueryPort.existsFavoriteUserAndStore(user, store);
    }
}
