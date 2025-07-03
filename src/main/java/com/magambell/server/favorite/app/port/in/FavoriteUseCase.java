package com.magambell.server.favorite.app.port.in;

public interface FavoriteUseCase {
    void registerFavorite(Long storeId, Long userId);
}
