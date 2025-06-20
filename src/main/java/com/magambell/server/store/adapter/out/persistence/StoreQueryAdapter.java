package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class StoreQueryAdapter implements StoreQueryPort {

    private final StoreRepository storeRepository;

    @Override
    public boolean existsByUser(final User user) {
        return storeRepository.existsByUser(user);
    }

    @Override
    public Optional<Store> getStoreByUser(final User user) {
        return storeRepository.findByUser(user);
    }

    @Override
    public List<StoreListDTOResponse> getStoreList(final SearchStoreListServiceRequest request) {
        return storeRepository.getStoreList(request);
    }

    @Override
    public StoreDetailDTO getStoreDetail(final Long storeId) {
        return storeRepository.getStoreDetail(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }
}
