package com.magambell.server.banner.app.port.out;

import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.dto.OpenRegionDTO;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.out.response.EditStoreImageResponseDTO;
import com.magambell.server.store.app.port.out.response.StoreRegisterResponseDTO;
import com.magambell.server.store.domain.entity.Store;

import java.util.List;

public interface BannerCommandPort {

    BannerRegisterResponseDTO registerBanner(RegisterBannerDTO dto);


}
