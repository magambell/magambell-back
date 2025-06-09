package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.store.app.port.out.response.PreSignedUrlImage;
import java.util.List;

public record StoreImagesResponse(List<PreSignedUrlImage> preSignedUrlImages) {
}
