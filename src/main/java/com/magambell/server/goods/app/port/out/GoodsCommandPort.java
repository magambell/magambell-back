package com.magambell.server.goods.app.port.out;

import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.app.port.out.response.EditGoodsImageResponseDTO;
import com.magambell.server.goods.app.port.out.response.GoodsRegisterResponseDTO;
import com.magambell.server.goods.domain.entity.Goods;

import java.util.List;

public interface GoodsCommandPort {
    GoodsRegisterResponseDTO registerGoods(RegisterGoodsDTO dto);

    EditGoodsImageResponseDTO editGoodsImage(Goods goods, List<GoodsImagesRegister> goodsImagesRegisters);


}
