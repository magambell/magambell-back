package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.common.s3.S3InputPort;
import com.magambell.server.common.s3.dto.TransformedImageDTO;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.out.StoreCommandPort;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import com.magambell.server.store.app.port.out.response.StoreRegisterResponseDTO;
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

    private static final String IMAGE_PREFIX = "STORE";

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final S3InputPort s3InputPort;

    @Override
    public StoreRegisterResponseDTO registerStore(final RegisterStoreDTO dto) {
        User user = dto.user();
        Store store = dto.toEntity();
        user.addStore(store);
        List<TransformedImageDTO> transformedImageDTOS = s3InputPort.saveImages(IMAGE_PREFIX,
                dto.toImage(), user);
        List<StorePreSignedUrlImage> storePreSignedUrlImages = addImagesAndGetPreSignedUrlImage(transformedImageDTOS,
                store);

        storeRepository.save(store);

        return new StoreRegisterResponseDTO(store.getId(), storePreSignedUrlImages);
    }

    @Override
    public void storeApprove(final Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        store.approve();
    }

    private List<StorePreSignedUrlImage> addImagesAndGetPreSignedUrlImage(final List<TransformedImageDTO> imageDTOList,
                                                                          final Store store) {
        return imageDTOList.stream()
                .map(imageDTO -> {
                    store.addStoreImage(StoreImage.create(imageDTO.getUrl(), imageDTO.id()));
                    return new StorePreSignedUrlImage(imageDTO.id(), imageDTO.putUrl());
                })
                .toList();
    }
}
