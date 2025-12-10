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
import java.util.Map;
import java.util.stream.Collectors;

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
        // 기존 이미지 조회
        List<GoodsImage> existingImages = goodsImageRepository.findByGoodsId(goods.getId());
        Map<String, GoodsImage> existingImageMap = existingImages.stream()
                .collect(Collectors.toMap(
                        img -> extractKeyFromUrl(img.getImageUrl()),
                        img -> img
                ));

        List<GoodsPreSignedUrlImage> goodsPreSignedUrlImages = new ArrayList<>();
        List<GoodsImage> newGoodsImageList = new ArrayList<>();

        for (int i = 0; i < goodsImagesRegisters.size(); i++) {
            GoodsImagesRegister register = goodsImagesRegisters.get(i);
            
            // 기존 이미지인지 확인
            if (existingImageMap.containsKey(register.key())) {
                // 기존 이미지 - presignedUrl 재생성하지 않고 기존 것 유지
                GoodsImage existingImage = existingImageMap.get(register.key());
                
                // name이 변경되었는지 확인
                if (!existingImage.getGoodsName().equals(register.goodsName())) {
                    existingImage.setGoodsName(register.goodsName());
                }
                
                newGoodsImageList.add(existingImage);
                // 기존 이미지는 presignedUrl 생성하지 않음 (null 또는 빈 값)
                goodsPreSignedUrlImages.add(new GoodsPreSignedUrlImage(register.id(), null));
                
                // 유지되는 이미지는 맵에서 제거
                existingImageMap.remove(register.key());
            } else {
                // 새 이미지 - S3에 업로드하고 presignedUrl 생성
                List<ImageRegister> imageToUpload = List.of(new ImageRegister(register.id(), register.key()));
                List<TransformedImageDTO> transformedImageDTOS = s3InputPort.saveImages(IMAGE_PREFIX, imageToUpload, goods.getId());
                
                if (!transformedImageDTOS.isEmpty()) {
                    TransformedImageDTO transformedImage = transformedImageDTOS.get(0);
                    
                    GoodsImage newImage = GoodsImage.builder()
                            .goods(goods)
                            .goodsName(register.goodsName())
                            .imageUrl(transformedImage.getUrl())
                            .build();
                    newGoodsImageList.add(newImage);
                    
                    goodsPreSignedUrlImages.add(new GoodsPreSignedUrlImage(transformedImage.id(), transformedImage.putUrl()));
                }
            }
        }

        // DB 업데이트 (삭제된 이미지는 DB에서만 제거, S3는 유지)
        goodsImageRepository.deleteGoodsImageByGoodsId(goods.getId());
        goodsImageRepository.saveAll(newGoodsImageList);

        return new EditGoodsImageResponseDTO(goods.getId(), goodsPreSignedUrlImages);
    }

    private String extractKeyFromUrl(String imageUrl) {
        // URL에서 key 추출 (예: https://d1xe26zpyg8fzv.cloudfront.net/GOODS/123/1_image.jpg -> 1_image.jpg)
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "";
        }
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastSlashIndex + 1);
        }
        return imageUrl;
    }


    private List<GoodsPreSignedUrlImage> addImagesAndGetPreSignedUrlImage(final List<TransformedImageDTO> imageDTOList) {
        return imageDTOList.stream()
                .map(imageDTO -> {
                    return new GoodsPreSignedUrlImage(imageDTO.id(), imageDTO.putUrl());
                })
                .toList();
    }
}
