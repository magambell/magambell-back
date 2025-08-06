package com.magambell.server.review.app.port.out.response;

import com.magambell.server.review.adapter.out.persistence.ReviewRegisterResponse;
import java.util.List;

public record ReviewRegisterResponseDTO(
        Long id,
        List<ReviewPreSignedUrlImage> reviewPreSignedUrlImages
) {
    public ReviewRegisterResponse toResponse() {
        return new ReviewRegisterResponse(String.valueOf(id), reviewPreSignedUrlImages);
    }
}
