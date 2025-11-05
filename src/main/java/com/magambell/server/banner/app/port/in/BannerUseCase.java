package com.magambell.server.banner.app.port.in;

import com.magambell.server.banner.adaper.out.persistence.BannerImageResponse;
import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;

public interface BannerUseCase {
    BannerImageResponse registerBanner(RegisterBannerServiceRequest request);

    BannerImagesResponse getBannerImageList();


}
