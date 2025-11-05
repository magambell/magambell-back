package com.magambell.server.banner.app.port.in;

import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.request.*;
import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;

import java.util.List;

public interface BannerUseCase {
    BannerImagesResponse registerBanner(RegisterBannerServiceRequest request);

}
