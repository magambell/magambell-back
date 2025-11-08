package com.magambell.server.banner.adaper.out.persistence;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;
import com.magambell.server.banner.app.port.out.BannerCommandPort;
import com.magambell.server.banner.app.port.out.response.BannerPreSignedUrlImage;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.banner.app.port.out.response.EditBannerImageResponseDTO;
import com.magambell.server.banner.domain.entity.Banner;
import com.magambell.server.banner.domain.repository.BannerRepository;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.s3.S3InputPort;
import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
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
        Banner banner = bannerRepository.save(Banner.create(dto.bannerImagesRegister().id()));
        TransformedImageDTO transformedImageDTO = s3InputPort.saveImage(IMAGE_PREFIX, dto.toImage(), banner.getId());
        banner.modifyUrl(transformedImageDTO.getUrl());

        BannerPreSignedUrlImage bannerPreSignedUrlImage = getBannerPreSignedUrlImage(banner.getId(), transformedImageDTO);

        return new BannerRegisterResponseDTO(banner.getId(), bannerPreSignedUrlImage);
    }

    @Override
    public List<Banner> getBannerList() {
        return bannerRepository.getBannerList();
    }

    @Override
    public EditBannerImageResponseDTO editBannerImage(Banner banner, BannerImagesRegister bannerImagesRegister) {

        s3InputPort.deleteS3Objects(IMAGE_PREFIX, banner.getId());

        ImageRegister imageRegister = new ImageRegister(bannerImagesRegister.id(), bannerImagesRegister.key());
        TransformedImageDTO transformedImageDTO = s3InputPort.saveImage(IMAGE_PREFIX, imageRegister, banner.getId());
        banner.modifyUrl(transformedImageDTO.getUrl());

        BannerPreSignedUrlImage bannerPreSignedUrlImage = getBannerPreSignedUrlImage(banner.getId(), transformedImageDTO);


        return new EditBannerImageResponseDTO(banner.getId(), bannerPreSignedUrlImage);
    }

    @Override
    public void delete(Banner banner) {
        s3InputPort.deleteS3Objects(IMAGE_PREFIX, banner.getId());
        bannerRepository.delete(banner);
    }


    private BannerPreSignedUrlImage getBannerPreSignedUrlImage(final Long bannerId, final TransformedImageDTO imageDTO) {
        return new BannerPreSignedUrlImage(bannerId, imageDTO.id(), imageDTO.putUrl());
    }
}
