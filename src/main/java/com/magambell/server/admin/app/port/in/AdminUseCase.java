package com.magambell.server.admin.app.port.in;

import com.magambell.server.admin.adapter.out.persistence.AdminEditStoreResponse;
import com.magambell.server.admin.app.port.in.request.AdminEditStoreServiceRequest;
import com.magambell.server.admin.app.port.out.response.AdminStatsResponse;
import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;

public interface AdminUseCase {
    AdminStatsResponse getStats();
    AdminEditStoreResponse editStore(Long storeId, AdminEditStoreServiceRequest request);
    StoreAdminListResponse getAllApprovedStores();
}
