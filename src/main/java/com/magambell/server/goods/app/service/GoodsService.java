package com.magambell.server.goods.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.goods.app.port.in.GoodsUseCase;
import com.magambell.server.goods.app.port.in.request.ChangeGoodsStatusServiceRequest;
import com.magambell.server.goods.app.port.in.request.EditGoodsServiceRequest;
import com.magambell.server.goods.app.port.in.request.RegisterGoodsServiceRequest;
import com.magambell.server.goods.app.port.out.GoodsCommandPort;
import com.magambell.server.goods.app.port.out.GoodsQueryPort;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.notification.app.port.in.NotificationUseCase;
import com.magambell.server.notification.app.port.in.request.NotifyStoreOpenRequest;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GoodsService implements GoodsUseCase {

    private final UserQueryPort userQueryPort;
    private final StoreQueryPort storeQueryPort;
    private final GoodsCommandPort goodsCommandPort;
    private final GoodsQueryPort goodsQueryPort;
    private final NotificationUseCase notificationUseCase;

    @Transactional
    @Override
    public void registerGoods(final RegisterGoodsServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        Store store = getStore(user);
        existGoods(store);
        goodsCommandPort.registerGoods(request.toDTO(store));
    }

    @Transactional
    @Override
    public void changeGoodsStatus(final ChangeGoodsStatusServiceRequest request, final LocalDateTime today) {
        User user = userQueryPort.findById(request.userId());
        Goods goods = goodsQueryPort.findWithStoreAndUserById(request.goodsId());
        goods.changeStatus(user, request.saleStatus(), today);

        if (request.saleStatus() == SaleStatus.ON) {
            notificationUseCase.notifyStoreOpen(new NotifyStoreOpenRequest(
                    goods.getStore(),
                    goods.getStore().getName() + " 매장이 오픈했습니다.",
                    goods.getStore().getName() + " 매장이 오픈했습니다."
            ));
        }
    }

    @Transactional
    @Override
    public void editGoods(final EditGoodsServiceRequest request) {
        Goods goods = goodsQueryPort.findOwnedGoodsWithRelations(request.goodsId(), request.userId());
        goods.edit(request.toDTO());
    }

    private Store getStore(final User user) {
        return storeQueryPort.getStoreByUser(user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
    }

    private void existGoods(final Store store) {
        if (goodsQueryPort.existsByStoreId(store.getId())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_GOODS);
        }
    }
}
