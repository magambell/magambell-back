package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import jakarta.validation.constraints.*;

import java.util.List;

public record RegisterReviewRequest(

        @NotNull(message = "주문을 선택해 주세요.")
        Long orderGoodsId,

        @NotNull(message = "서비스는 어떠셨을까요를 선택해 주세요.")
        @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
        @Max(value = 3, message = "평점은 3 이하여야 합니다.")
        Integer rating,

        @NotBlank(message = "구매 후기를 작성해 주세요.")
        String description,

        @Size(max = 10, message = "이미지는 최대 10장까지 등록할 수 있습니다.")
        List<ReviewImageRegister> reviewImageRegisters
) {

    public RegisterReviewServiceRequest toServiceRequest() {
        return new RegisterReviewServiceRequest(orderGoodsId, rating, description,
                reviewImageRegisters);
    }
}
