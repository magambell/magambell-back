package com.magambell.server.banner.app.port.out.response;

public record BannerPreSignedUrlImage(
        Long bannerId,
        Integer id,
        String url
) {
}
