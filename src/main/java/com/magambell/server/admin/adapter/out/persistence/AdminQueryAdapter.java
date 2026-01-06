package com.magambell.server.admin.adapter.out.persistence;

import com.magambell.server.admin.app.port.out.AdminQueryPort;
import com.magambell.server.admin.domain.repository.AdminRepository;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class AdminQueryAdapter implements AdminQueryPort {

    private final AdminRepository adminRepository;
    private final StoreRepository storeRepository;

    @Override
    public Long countTotalUsers() {
        return adminRepository.countActiveUsers();
    }

    @Override
    public Long countTotalStores() {
        return adminRepository.countApprovedStores();
    }

    @Override
    public Store findStoreByIdWithGoods(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }
}
