package com.magambell.server.admin.app.port.out;

import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.domain.entity.Store;

import java.util.List;

public interface AdminCommandPort {
    void editStoreImages(Store store, List<StoreImagesRegister> storeImagesRegisters);
    void editGoodsImages(Goods goods, List<GoodsImagesRegister> goodsImagesRegisters);
}
