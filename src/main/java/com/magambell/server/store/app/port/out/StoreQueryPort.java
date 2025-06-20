package com.magambell.server.store.app.port.out;

import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface StoreQueryPort {
    boolean existsByUser(User user);

    Optional<Store> getStoreByUser(User user);

    List<StoreListDTOResponse> getStoreList(SearchStoreListServiceRequest request);

    StoreDetailDTO getStoreDetail(Long storeId);
}
