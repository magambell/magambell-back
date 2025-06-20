package com.magambell.server.store.domain.repository;

import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import java.util.List;
import java.util.Optional;

public interface StoreRepositoryCustom {

    List<StoreListDTOResponse> getStoreList(SearchStoreListServiceRequest request);

    Optional<StoreDetailDTO> getStoreDetail(Long storeId);
}
