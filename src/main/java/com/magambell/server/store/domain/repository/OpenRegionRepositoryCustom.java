package com.magambell.server.store.domain.repository;

import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.app.port.in.dto.OpenRegionDTO;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.OpenRegionListServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StoreAdminListDTO;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.entity.StoreImage;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OpenRegionRepositoryCustom {

    List<OpenRegionListDTO> getOpenRegionList(OpenRegionListServiceRequest request, Pageable pageable);

}
