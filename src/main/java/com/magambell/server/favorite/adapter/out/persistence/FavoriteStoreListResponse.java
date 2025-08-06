package com.magambell.server.favorite.adapter.out.persistence;

import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import java.util.List;

public record FavoriteStoreListResponse(List<FavoriteStoreListDTOResponse> favoriteStoreListDTOResponseList) {
}
