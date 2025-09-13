package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreAdminListDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.entity.StoreImage;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

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
    public List<StoreListDTOResponse> getStoreList(final SearchStoreListServiceRequest request,
                                                   final Pageable pageable) {
        return storeRepository.getStoreList(request, pageable);
    }

    @Override
    public StoreDetailResponse getStoreDetail(final Long storeId) {
        return storeRepository.getStoreDetail(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }

    @Override
    public Store findById(final Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }

    @Override
    public OwnerStoreDetailDTO getOwnerStoreInfo(final User user) {
        return storeRepository.getOwnerStoreInfo(user.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }

    @Override
    public List<StoreListDTOResponse> getCloseStoreList(final CloseStoreListServiceRequest request) {
        return storeRepository.getCloseStoreList(request);
    }

    @Override
    public List<StoreAdminListDTO> getWaitingStoreList(final Pageable pageable) {
        return storeRepository.getWaitingStoreList(pageable);
    }

    @Override
    public Long findOwnerIdByStoreId(final Long storeId) {
        return storeRepository.findOwnerIdByStoreId(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }

    @Override
    public List<StoreImage> getStoreImageList(final Long storeId) {
        return storeRepository.getStoreImageList(storeId);
    }

    @Override
    public Store getStoreAndStoreImages(final Long storeId) {
        return storeRepository.getStoreAndStoreImages(storeId);
    }
}
