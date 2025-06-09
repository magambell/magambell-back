package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class StoreQueryAdapter implements StoreQueryPort {

    private final StoreRepository storeRepository;

    @Override
    public boolean existsByUser(final User user) {
        return storeRepository.existsByUser(user);
    }
}
