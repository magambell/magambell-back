package com.magambell.server.review.adapter.out.persistence;

import com.magambell.server.review.app.port.out.response.ReviewReportListDTO;

import java.util.List;

public record ReviewReportListResponse(List<ReviewReportListDTO> reviewReportListResponse) {
}
