package com.magambell.server.banner.adaper.out.persistence;

import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;
import com.magambell.server.banner.app.port.out.BannerCommandPort;
import com.magambell.server.banner.app.port.out.response.BannerPreSignedUrlImage;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.banner.domain.entity.Banner;
import com.magambell.server.banner.domain.repository.BannerRepository;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.s3.S3InputPort;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import com.magambell.server.store.app.port.out.response.StoreRegisterResponseDTO;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.entity.StoreImage;
import com.magambell.server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Adapter
public class BannerCommandAdapter implements BannerCommandPort {

    private static final String IMAGE_PREFIX = "BANNER";

    private final BannerRepository bannerRepository;
    private final S3InputPort s3InputPort;

    @Override
    public BannerRegisterResponseDTO registerBanner(final RegisterBannerDTO dto) {
        TransformedImageDTO transformedImageDTO = s3InputPort.saveImage(IMAGE_PREFIX, dto.toImage());

        Banner banner = bannerRepository.save(Banner.create(dto.name(), dto.order()));
        BannerPreSignedUrlImage bannerPreSignedUrlImage = getPreSignedUrlImage(transformedImageDTO);

        return new BannerRegisterResponseDTO(banner.getId(), bannerPreSignedUrlImage);
    }

    @Override
    public List<Banner> getBannerList() {
        return bannerRepository.getBannerList();
    }


    private BannerPreSignedUrlImage getPreSignedUrlImage(final TransformedImageDTO imageDTO) {
        return new BannerPreSignedUrlImage(imageDTO.id(), imageDTO.putUrl());
    }
}
