package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.store.app.port.in.S3InputPort;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.in.dto.TransformedImageDTO;
import com.magambell.server.store.app.port.out.StoreCommandPort;
import com.magambell.server.store.app.port.out.response.PreSignedUrlImage;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.store.domain.model.StoreImage;
import com.magambell.server.store.domain.repository.StoreImageRepository;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class StoreCommandAdapter implements StoreCommandPort {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final S3InputPort s3InputPort;

    @Override
    public List<PreSignedUrlImage> registerStore(final RegisterStoreDTO dto, final User user) {
        Store store = dto.toEntity();
        List<TransformedImageDTO> transformedImageDTOS = s3InputPort.saveImages(dto.storeImagesRegisters(), user);
        List<PreSignedUrlImage> preSignedUrlImages = addImagesAndGetPreSignedUrlImage(transformedImageDTOS, store);

        storeRepository.save(store);

        return preSignedUrlImages;
    }

    private List<PreSignedUrlImage> addImagesAndGetPreSignedUrlImage(final List<TransformedImageDTO> imageDTOList,
                                                                     final Store store) {
        return imageDTOList.stream()
                .map(imageDTO -> {
                    store.addStoreImage(StoreImage.create(imageDTO.getUrl(), imageDTO.id()));
                    return new PreSignedUrlImage(imageDTO.id(), imageDTO.putUrl());
                })
                .toList();
    }
}
