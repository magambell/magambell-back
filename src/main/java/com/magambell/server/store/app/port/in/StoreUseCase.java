package com.magambell.server.store.app.port.in;

import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.StoreApproveServiceRequest;
import com.magambell.server.store.app.port.in.request.WaitingStoreListServiceRequest;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;

public interface StoreUseCase {
    StoreImagesResponse registerStore(RegisterStoreServiceRequest request, Long userId);

    StoreListResponse getStoreList(SearchStoreListServiceRequest request);

    void storeApprove(StoreApproveServiceRequest request);

    StoreDetailResponse getStoreDetail(Long storeId);

    OwnerStoreDetailDTO getOwnerStoreInfo(Long userId);

    StoreListResponse getCloseStoreList(CloseStoreListServiceRequest request);

    StoreAdminListResponse getWaitingStoreList(WaitingStoreListServiceRequest request);

    StoreImagesResponse getStoreImageList(Long userId, Long storeId);
}
