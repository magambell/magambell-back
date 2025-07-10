package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record RegisterReviewRequest(

        @NotNull(message = "주문을 선택해 주세요.")
        Long orderGoodsId,

        @Positive(message = "서비스는 어떠셨을까요를 선택해 주세요.")
        Integer rating,

        List<SatisfactionReason> satisfactionReasons,

        @NotBlank(message = "구매 후기를 작성해 주세요.")
        String description,

        @NotEmpty(message = "대표 이미지는 필수입니다.")
        List<ReviewImageRegister> reviewImageRegisters
) {

    public RegisterReviewServiceRequest toServiceRequest() {
        return new RegisterReviewServiceRequest(orderGoodsId, rating, satisfactionReasons, description,
                reviewImageRegisters);
    }
}
