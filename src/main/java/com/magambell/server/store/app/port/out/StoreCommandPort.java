package com.magambell.server.store.app.port.out;

import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.user.domain.model.User;

public interface StoreCommandPort {
    void registerStore(RegisterStoreDTO dto, User user);
}
