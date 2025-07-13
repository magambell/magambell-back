package com.magambell.server.notification.app.port.out;

import com.magambell.server.notification.app.port.out.dto.FcmTokenDTO;
import com.magambell.server.notification.domain.model.FcmToken;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.domain.model.User;
import java.util.List;

public interface NotificationQueryPort {
    boolean existsByUserAndStore(User user, Store store);

    List<FcmTokenDTO> findWithAllByStoreId(Store store);

    FcmTokenDTO findWithAllByUserIdAndStoreIsNull(User user);

    List<FcmTokenDTO> findWithAllByOwnerIdsAndStoreIsNull(List<Long> ownerList);

    List<FcmToken> findByUserId(Long userId);
}
