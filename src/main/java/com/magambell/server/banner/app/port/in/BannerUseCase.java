package com.magambell.server.banner.app.port.in;

import com.magambell.server.banner.adaper.out.persistence.BannerImageResponse;
import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.request.DeleteBannerServiceRequest;
import com.magambell.server.banner.app.port.in.request.EditBannerImageServiceRequest;
import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;

public interface BannerUseCase {
    BannerImageResponse registerBanner(RegisterBannerServiceRequest request);

    BannerImagesResponse getBannerImageList();

    BannerImageResponse editBannerImage(EditBannerImageServiceRequest request);

    void deleteBanner(DeleteBannerServiceRequest request);

}
