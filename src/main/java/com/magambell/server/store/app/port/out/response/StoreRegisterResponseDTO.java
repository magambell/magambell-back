package com.magambell.server.store.app.port.out.response;

import java.util.List;

public record StoreRegisterResponseDTO(
        Long id,
        List<PreSignedUrlImage> preSignedUrlImages
) {
}
