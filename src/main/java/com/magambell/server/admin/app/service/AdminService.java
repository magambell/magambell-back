package com.magambell.server.admin.app.service;

import com.magambell.server.admin.app.port.in.AdminUseCase;
import com.magambell.server.admin.app.port.in.dto.AdminEditStoreDTO;
import com.magambell.server.admin.app.port.in.request.AdminEditStoreServiceRequest;
import com.magambell.server.admin.app.port.out.AdminCommandPort;
import com.magambell.server.admin.app.port.out.AdminQueryPort;
import com.magambell.server.admin.app.port.out.response.AdminStatsResponse;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.common.swagger.BaseResponse;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.store.domain.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminService implements AdminUseCase {

    private final AdminQueryPort adminQueryPort;
    private final AdminCommandPort adminCommandPort;

    @Override
    public AdminStatsResponse getStats() {
        Long totalUserCount = adminQueryPort.countTotalUsers();
        Long totalStoreCount = adminQueryPort.countTotalStores();
        
        return new AdminStatsResponse(totalUserCount, totalStoreCount);
    }

    @Transactional
    @Override
    public BaseResponse editStore(Long storeId, AdminEditStoreServiceRequest request) {
        AdminEditStoreDTO dto = request.toDTO();
        
        // Store 조회
        Store store = adminQueryPort.findStoreByIdWithGoods(storeId);
        
        // Store 기본 정보 수정
        store.editStoreInfo(
                dto.storeName(),
                dto.address(),
                dto.latitude(),
                dto.longitude(),
                dto.ownerName(),
                dto.ownerPhone(),
                dto.businessNumber(),
                dto.bankName(),
                dto.bankAccount(),
                dto.description(),
                dto.parkingDescription()
        );
        
        // Store 이미지 수정
        if (dto.storeImages() != null && !dto.storeImages().isEmpty()) {
            adminCommandPort.editStoreImages(store, dto.storeImages());
        }
        
        // Goods 수정
        if (!store.getGoods().isEmpty()) {
            Goods goods = store.getGoods().get(0);
            goods.editByAdmin(
                    dto.goodsName(),
                    dto.startTime(),
                    dto.endTime(),
                    dto.originalPrice(),
                    dto.discount(),
                    dto.salePrice(),
                    dto.quantity(),
                    dto.saleStatus()
            );
            
            // Goods 이미지 수정
            if (dto.goodsImages() != null && !dto.goodsImages().isEmpty()) {
                adminCommandPort.editGoodsImages(goods, dto.goodsImages());
            }
        }
        
        return new BaseResponse();
    }
}
