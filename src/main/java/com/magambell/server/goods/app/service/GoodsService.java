package com.magambell.server.goods.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.app.port.in.GoodsUseCase;
import com.magambell.server.goods.app.port.in.request.RegisterGoodsServiceRequest;
import com.magambell.server.goods.app.port.out.GoodsCommandPort;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GoodsService implements GoodsUseCase {

    private final UserQueryPort userQueryPort;
    private final StoreQueryPort storeQueryPort;
    private final GoodsCommandPort goodsCommandPort;

    @Transactional
    @Override
    public void registerGoods(final RegisterGoodsServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        Store store = getStore(user);
        goodsCommandPort.registerGoods(request.toDTO(store));
    }

    private Store getStore(final User user) {
        return storeQueryPort.getStoreByUser(user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }
}
