package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.store.app.port.out.response.StoreAdminListDTO;
import java.util.List;

public record StoreAdminListResponse(List<StoreAdminListDTO> storeAdminListDTOs) {
}
