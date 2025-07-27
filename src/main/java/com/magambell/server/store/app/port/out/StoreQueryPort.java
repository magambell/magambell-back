package com.magambell.server.store.app.port.out;

import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface StoreQueryPort {
    boolean existsByUser(User user);

    Optional<Store> getStoreByUser(User user);

    List<StoreListDTOResponse> getStoreList(SearchStoreListServiceRequest request, Pageable pageable);

    StoreDetailResponse getStoreDetail(Long storeId);

    Store findById(Long storeId);

    OwnerStoreDetailDTO getOwnerStoreInfo(User user);

    List<StoreListDTOResponse> getCloseStoreList(CloseStoreListServiceRequest request);

    List<StoreListDTOResponse> getWaitingStoreList(Pageable pageable);
}
