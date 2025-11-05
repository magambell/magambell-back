package com.magambell.server.banner.adaper.out.persistence;

import com.magambell.server.banner.app.port.out.response.BannerPreSignedUrlImage;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;

import java.util.List;

public record BannerImagesResponse(
        List<BannerPreSignedUrlImage> bannerPreSignedUrlImages
) {
}
