package com.magambell.server.admin.app.port.in;

import com.magambell.server.admin.app.port.in.request.AdminEditStoreServiceRequest;
import com.magambell.server.admin.app.port.out.response.AdminStatsResponse;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;

public interface AdminUseCase {
    AdminStatsResponse getStats();
    BaseResponse editStore(Long storeId, AdminEditStoreServiceRequest request);
    StoreAdminListResponse getAllApprovedStores();
}
