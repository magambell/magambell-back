package com.magambell.server.notification.domain.repository;

import com.magambell.server.notification.app.port.out.dto.FcmTokenDTO;
import java.util.List;

public interface FcmTokenRepositoryCustom {
    List<FcmTokenDTO> findWithAllByStoreId(Long storeId);

    FcmTokenDTO findWithAllByUserIdAndStoreIsNull(Long userId);

    List<FcmTokenDTO> findWithAllByOwnerIdsAndStoreIsNull(List<Long> ownerList);
}
