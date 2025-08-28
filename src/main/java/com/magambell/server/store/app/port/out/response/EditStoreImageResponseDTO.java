package com.magambell.server.store.app.port.out.response;

import java.util.List;

public record EditStoreImageResponseDTO(Long id, List<StorePreSignedUrlImage> storePreSignedUrlImages) {
}
