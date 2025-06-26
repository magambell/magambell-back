package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import java.util.List;

public record StoreImagesResponse(
        String id,
        List<StorePreSignedUrlImage> storePreSignedUrlImages
) {
}
