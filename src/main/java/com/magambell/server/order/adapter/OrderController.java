package com.magambell.server.order.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.common.security.CustomUserDetails;
import com.magambell.server.order.adapter.in.web.CreateOrderRequest;
import com.magambell.server.order.adapter.out.persistence.CreateOrderResponse;
import com.magambell.server.order.adapter.out.persistence.OrderListResponse;
import com.magambell.server.order.app.port.in.OrderUseCase;
import com.magambell.server.order.app.port.out.response.CreateOrderResponseDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Order API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@RestController
public class OrderController {

    private final OrderUseCase orderUseCase;

    @Operation(summary = "주문 등록")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = CreateOrderResponse.class))})
    @PostMapping("")
    public Response<CreateOrderResponse> createOrder(
            @RequestBody @Validated final CreateOrderRequest request,
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        CreateOrderResponseDTO dto = orderUseCase.createOrder(request.toServiceRequest(), customUserDetails.userId());
        return new Response<>(new CreateOrderResponse(dto.merchantUid(), dto.totalAmount()));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "고객 주문내역")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = OrderListResponse.class))})
    @GetMapping("")
    public Response<OrderListResponse> getOrderList(
            @AuthenticationPrincipal final CustomUserDetails customUserDetails
    ) {
        List<OrderListDTO> orderList = orderUseCase.getOrderList(customUserDetails.userId());
        return new Response<>(new OrderListResponse(orderList));
    }
}
