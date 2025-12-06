package com.magambell.server.goods.app.port.in;

import com.magambell.server.goods.adapter.out.persistence.GoodsImagesResponse;
import com.magambell.server.goods.app.port.in.request.ChangeGoodsStatusServiceRequest;
import com.magambell.server.goods.app.port.in.request.EditGoodsImagesServiceRequest;
import com.magambell.server.goods.app.port.in.request.EditGoodsServiceRequest;
import com.magambell.server.goods.app.port.in.request.RegisterGoodsServiceRequest;

import java.time.LocalDateTime;

public interface GoodsUseCase {
    GoodsImagesResponse registerGoods(RegisterGoodsServiceRequest request, Long userId);

    void changeGoodsStatus(ChangeGoodsStatusServiceRequest request, LocalDateTime today);

    GoodsImagesResponse editGoods(EditGoodsServiceRequest request);

    GoodsImagesResponse editGoodsImages(EditGoodsImagesServiceRequest request);

    void changeSaleStatusToOff(LocalDateTime now);
}
