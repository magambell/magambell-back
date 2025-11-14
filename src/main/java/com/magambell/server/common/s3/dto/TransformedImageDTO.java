package com.magambell.server.common.s3.dto;


public record TransformedImageDTO(
        Integer id,
        String getUrl,
        String putUrl
) {
}
