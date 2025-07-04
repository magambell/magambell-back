package com.magambell.server.goods.app.port.in;

import com.magambell.server.goods.app.port.in.request.ChangeGoodsStatusServiceRequest;
import com.magambell.server.goods.app.port.in.request.EditGoodsServiceRequest;
import com.magambell.server.goods.app.port.in.request.RegisterGoodsServiceRequest;
import java.time.LocalDateTime;

public interface GoodsUseCase {
    void registerGoods(RegisterGoodsServiceRequest request, Long userId);

    void changeGoodsStatus(ChangeGoodsStatusServiceRequest request, LocalDateTime today);

    void editGoods(EditGoodsServiceRequest request);
}
