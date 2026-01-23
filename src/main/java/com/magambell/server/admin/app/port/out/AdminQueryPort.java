package com.magambell.server.admin.app.port.out;

import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;
import com.magambell.server.store.domain.entity.Store;

public interface AdminQueryPort {
    Long countTotalUsers();
    Long countTotalStores();
    Store findStoreByIdWithGoods(Long storeId);
    StoreAdminListResponse findAllApprovedStores();
}
