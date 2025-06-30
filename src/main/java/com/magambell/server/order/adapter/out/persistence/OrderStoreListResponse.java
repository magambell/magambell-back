package com.magambell.server.order.adapter.out.persistence;

import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import java.util.List;

public record OrderStoreListResponse(List<OrderStoreListDTO> orderStoreList) {
}
