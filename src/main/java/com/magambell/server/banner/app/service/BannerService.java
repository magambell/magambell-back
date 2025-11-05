package com.magambell.server.banner.app.service;

import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.BannerUseCase;
import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import com.magambell.server.banner.app.port.out.BannerCommandPort;
import com.magambell.server.banner.app.port.out.BannerQueryPort;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.user.app.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BannerService implements BannerUseCase {

    private final BannerCommandPort bannerCommandPort;
//    private final BannerQueryPort bannerQueryPort;
//    private final UserQueryPort userQueryPort;


    @Transactional
    @Override
    public BannerImagesResponse registerBanner(final RegisterBannerServiceRequest request) {
        BannerRegisterResponseDTO bannerRegisterResponseDTO = bannerCommandPort.registerBanner(
                request.toBannerDTO());

        return new BannerImagesResponse(String.valueOf(bannerRegisterResponseDTO.id()),
                bannerRegisterResponseDTO.bannerPreSignedUrlImage());
    }


//    private EditBannerImageResponseDTO changeBannerImage(final Banner banner,
//                                                       final List<BannerImagesRegister> bannerImagesRegisters) {
//        return bannerCommandPort.editBannerImage(banner, bannerImagesRegisters);
//    }
}
