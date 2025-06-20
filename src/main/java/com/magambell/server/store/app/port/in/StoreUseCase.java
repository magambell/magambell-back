package com.magambell.server.store.app.port.in;

import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.app.port.in.request.StoreApproveServiceRequest;
import com.magambell.server.store.app.port.out.dto.StoreDetailDTO;

public interface StoreUseCase {
    StoreImagesResponse registerStore(RegisterStoreServiceRequest request, Long userId);

    StoreListResponse getStoreList(SearchStoreListServiceRequest request);

    void storeApprove(StoreApproveServiceRequest request);

    StoreDetailDTO getStoreDetail(Long storeId);
}
