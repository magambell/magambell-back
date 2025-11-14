package com.magambell.server.goods.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.s3.S3InputPort;
import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.app.port.out.GoodsCommandPort;
import com.magambell.server.goods.app.port.out.response.EditGoodsImageResponseDTO;
import com.magambell.server.goods.app.port.out.response.GoodsPreSignedUrlImage;
import com.magambell.server.goods.app.port.out.response.GoodsRegisterResponseDTO;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.goods.domain.entity.GoodsImage;
import com.magambell.server.goods.domain.repository.GoodsImageRepository;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.store.domain.entity.Store;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Adapter
public class GoodsCommandAdapter implements GoodsCommandPort {

    private static final String IMAGE_PREFIX = "GOODS";

    private final GoodsRepository goodsRepository;
    private final S3InputPort s3InputPort;
    private final GoodsImageRepository goodsImageRepository;

    @Override
    public GoodsRegisterResponseDTO registerGoods(final RegisterGoodsDTO dto) {
        Store store = dto.store();
        Goods goods = dto.toGoods();
        List<GoodsImagesRegister> goodsImagesRegisters = dto.goodsImagesRegisters();

        store.addGoods(goods);
        Goods goodsEntity = goodsRepository.save(goods);

        List<TransformedImageDTO> transformedImageDTOS = s3InputPort.saveImages(IMAGE_PREFIX,
                dto.toImage(), goodsEntity.getId());

        List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages = addImagesAndGetPreSignedUrlImage(transformedImageDTOS);

        List<GoodsImage> goodsImageList = new ArrayList<>();
        for (int i = 0; i < goodsImagesRegisters.size(); i++) {
            goodsImageList.add(
                    GoodsImage.builder()
                            .goods(goodsEntity)
                            .goodsName(goodsImagesRegisters.get(i).goodsName())
                            .imageUrl(transformedImageDTOS.get(i).getUrl())
                            .build()
            );
        }

        goodsImageRepository.saveAll(goodsImageList);

        return new GoodsRegisterResponseDTO(goodsPreSignedUrlImages);
    }

    @Override
    public EditGoodsImageResponseDTO editGoodsImage(Goods goods, List<GoodsImagesRegister> goodsImagesRegisters) {

        s3InputPort.deleteS3Objects(IMAGE_PREFIX, goods.getId());

        List<ImageRegister> list = goodsImagesRegisters.stream()
                .map(goodsImagesRegister -> new ImageRegister(goodsImagesRegister.id(), goodsImagesRegister.key()))
                .toList();
        List<TransformedImageDTO> transformedImageDTOS = s3InputPort.saveImages(IMAGE_PREFIX, list, goods.getId());
        List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages = addImagesAndGetPreSignedUrlImage(transformedImageDTOS);

        goodsImageRepository.deleteGoodsImageByGoodsId(goods.getId());
        List<GoodsImage> goodsImageList = new ArrayList<>();
        for (int i = 0; i < goodsImagesRegisters.size(); i++) {
            goodsImageList.add(
                    GoodsImage.builder()
                            .goods(goods)
                            .goodsName(goodsImagesRegisters.get(i).goodsName())
                            .imageUrl(transformedImageDTOS.get(i).getUrl())
                            .build()
            );
        }

        goodsImageRepository.saveAll(goodsImageList);

        return new EditGoodsImageResponseDTO(goods.getId(), goodsPreSignedUrlImages);
    }


    private List<GoodsPreSignedUrlImage> addImagesAndGetPreSignedUrlImage(final List<TransformedImageDTO> imageDTOList) {
        return imageDTOList.stream()
                .map(imageDTO -> {
                    return new GoodsPreSignedUrlImage(imageDTO.id(), imageDTO.putUrl());
                })
                .toList();
    }
}
