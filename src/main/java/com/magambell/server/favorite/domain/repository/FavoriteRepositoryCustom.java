package com.magambell.server.favorite.domain.repository;

import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FavoriteRepositoryCustom {
    List<FavoriteStoreListDTOResponse> getFavoriteStoreList(Pageable pageable, Long userId);
}
