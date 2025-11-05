package com.magambell.server.banner.domain.repository;

import com.magambell.server.banner.domain.entity.Banner;
import com.magambell.server.store.app.port.in.request.OpenRegionListServiceRequest;
import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BannerRepositoryCustom {

    List<Banner> getBannerList();

}
