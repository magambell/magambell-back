package com.magambell.server.banner.app.port.out.response;

import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;

import java.util.List;

public record BannerRegisterResponseDTO(
        Long id,
        BannerPreSignedUrlImage bannerPreSignedUrlImage
) {
}
