package com.magambell.server.banner.adaper.out.persistence;

import com.magambell.server.banner.app.port.out.response.BannerPreSignedUrlImage;

public record BannerImageResponse(
        String id,
        BannerPreSignedUrlImage bannerPreSignedUrlImage
) {
}
