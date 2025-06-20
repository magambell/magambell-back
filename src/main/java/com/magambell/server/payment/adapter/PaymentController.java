package com.magambell.server.payment.adapter;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment", description = "Payment API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController {
}
