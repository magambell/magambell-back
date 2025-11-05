package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.store.app.port.out.response.OpenRegionListDTO;

import java.util.List;

public record OpenRegionListResponse(List<OpenRegionListDTO> openRegionListResponse) {
}
