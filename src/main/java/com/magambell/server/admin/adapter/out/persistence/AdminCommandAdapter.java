package com.magambell.server.admin.adapter.out.persistence;

import com.magambell.server.admin.app.port.out.AdminCommandPort;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.out.GoodsCommandPort;
import com.magambell.server.goods.app.port.out.response.EditGoodsImageResponseDTO;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.out.StoreCommandPort;
import com.magambell.server.store.app.port.out.response.EditStoreImageResponseDTO;
import com.magambell.server.store.domain.entity.Store;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Adapter
public class AdminCommandAdapter implements AdminCommandPort {

    private final StoreCommandPort storeCommandPort;
    private final GoodsCommandPort goodsCommandPort;

    @Override
    public EditStoreImageResponseDTO editStoreImages(Store store, List<StoreImagesRegister> storeImagesRegisters) {
        return storeCommandPort.editStoreImage(store, storeImagesRegisters);
    }

    @Override
    public EditGoodsImageResponseDTO editGoodsImages(Goods goods, List<GoodsImagesRegister> goodsImagesRegisters) {
        return goodsCommandPort.editGoodsImage(goods, goodsImagesRegisters);
    }
}
