package com.magambell.server.banner.app.port.out;

import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.OpenRegionListServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.response.*;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.entity.StoreImage;
import com.magambell.server.user.domain.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BannerQueryPort {

}
