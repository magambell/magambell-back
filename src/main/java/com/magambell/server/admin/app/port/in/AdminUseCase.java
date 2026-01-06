package com.magambell.server.admin.app.port.in;

import com.magambell.server.admin.app.port.in.request.AdminEditStoreServiceRequest;
import com.magambell.server.admin.app.port.out.response.AdminStatsResponse;
import com.magambell.server.common.swagger.BaseResponse;

public interface AdminUseCase {
    AdminStatsResponse getStats();
    BaseResponse editStore(Long storeId, AdminEditStoreServiceRequest request);
}
