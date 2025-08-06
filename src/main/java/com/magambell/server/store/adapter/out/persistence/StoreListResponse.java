package com.magambell.server.store.adapter.out.persistence;

import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import java.util.List;

public record StoreListResponse(List<StoreListDTOResponse> storeListDTOResponses) {
}
