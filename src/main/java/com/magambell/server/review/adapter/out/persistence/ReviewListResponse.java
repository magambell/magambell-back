package com.magambell.server.review.adapter.out.persistence;

import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import java.util.List;

public record ReviewListResponse(List<ReviewListDTO> reviewListDTOList) {
}
