package com.magambell.server.admin.app.service;

import com.magambell.server.admin.adapter.out.persistence.AdminEditStoreResponse;
import com.magambell.server.admin.app.port.in.AdminUseCase;
import com.magambell.server.admin.app.port.in.dto.AdminEditStoreDTO;
import com.magambell.server.admin.app.port.in.request.AdminEditStoreServiceRequest;
import com.magambell.server.admin.app.port.out.AdminCommandPort;
import com.magambell.server.admin.app.port.out.AdminQueryPort;
import com.magambell.server.admin.app.port.out.response.AdminStatsResponse;
import com.magambell.server.goods.app.port.out.response.EditGoodsImageResponseDTO;
import com.magambell.server.goods.app.port.out.response.GoodsPreSignedUrlImage;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.store.app.port.out.response.EditStoreImageResponseDTO;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;
import com.magambell.server.store.domain.entity.Store;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public AdminEditStoreResponse editStore(Long storeId, AdminEditStoreServiceRequest request) {
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
        
        List<StorePreSignedUrlImage> storePreSignedUrlImages = Collections.emptyList();
        List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages = Collections.emptyList();

        // Store 이미지 수정
        if (dto.storeImages() != null) {
            EditStoreImageResponseDTO storeImageResponse = adminCommandPort.editStoreImages(store, dto.storeImages());
            storePreSignedUrlImages = storeImageResponse.storePreSignedUrlImages();
        }
        
        // Goods 수정
        if (!store.getGoods().isEmpty()) {
            Goods goods = store.getGoods().get(0);
            String goodsName = resolveGoodsName(dto, goods);
            goods.editByAdmin(
                goodsName,
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
                EditGoodsImageResponseDTO goodsImageResponse = adminCommandPort.editGoodsImages(goods, dto.goodsImages());
                goodsPreSignedUrlImages = goodsImageResponse.goodsPreSignedUrlImages();
            }
        }
        
        return new AdminEditStoreResponse(storePreSignedUrlImages, goodsPreSignedUrlImages);
    }

    private String resolveGoodsName(final AdminEditStoreDTO dto, final Goods goods) {
        if (dto.goodsImages() == null || dto.goodsImages().isEmpty()) {
            return goods.getName();
        }

        return dto.goodsImages().stream()
                .map(img -> img.goodsName())
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(goods.getName());
    }

    @Override
    public StoreAdminListResponse getAllApprovedStores() {
        return adminQueryPort.findAllApprovedStores();
    }
}
