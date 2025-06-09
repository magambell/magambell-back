package com.magambell.server.store.app.port.in.dto;

public record TransformedImageDTO(
        Integer id,
        String getUrl,
        String putUrl
) {
}
