package com.magambell.server.store.domain.repository;

import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    List<StoreListDTOResponse> getStoreList(SearchStoreListServiceRequest request, Pageable pageable);

    Optional<StoreDetailDTO> getStoreDetail(Long storeId);
}
