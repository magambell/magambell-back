package com.magambell.server.favorite.app.port.out;

import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import com.magambell.server.favorite.domain.entity.Favorite;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FavoriteQueryPort {
    boolean existsFavoriteUserAndStore(User user, Store store);

    Favorite findByUserAndStore(User user, Store store);

    List<FavoriteStoreListDTOResponse> getFavoriteStoreList(Pageable pageable, User user);
}
