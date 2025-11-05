package com.magambell.server.banner.adaper.out.persistence;

import com.magambell.server.banner.app.port.out.BannerQueryPort;
import com.magambell.server.banner.domain.entity.Banner;
import com.magambell.server.banner.domain.repository.BannerRepository;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.OpenRegionListServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreAdminListDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.entity.StoreImage;
import com.magambell.server.store.domain.repository.OpenRegionRepository;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Adapter
public class BannerQueryAdapter implements BannerQueryPort {

    private final BannerRepository bannerRepository;

    @Override
    public List<Banner> getBannerList() {
        return bannerRepository.getBannerList();
    }

}
