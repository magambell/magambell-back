package com.magambell.server.favorite.app.port.in;

import com.magambell.server.favorite.app.port.in.request.FavoriteStoreListServiceRequest;
import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import java.util.List;

public interface FavoriteUseCase {
    void registerFavorite(Long storeId, Long userId);

    void deleteFavorite(Long storeId, Long userId);

    List<FavoriteStoreListDTOResponse> getFavoriteStoreList(FavoriteStoreListServiceRequest request);
}
