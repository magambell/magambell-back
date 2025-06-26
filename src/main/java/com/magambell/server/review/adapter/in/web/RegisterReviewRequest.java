package com.magambell.server.review.adapter.in.web;

import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.domain.enums.SatisfactionReason;
import com.magambell.server.review.domain.enums.ServiceSatisfaction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RegisterReviewRequest(

        @NotNull(message = "주문을 선택해 주세요.")
        Long orderId,

        @NotNull(message = "서비스는 어떠셨을까요를 선택해 주세요.")
        ServiceSatisfaction serviceSatisfaction,

        @NotNull(message = "만족스러웠던 점을 선택해 주세요.")
        SatisfactionReason satisfactionReason,

        String description,

        @NotEmpty(message = "대표 이미지는 필수입니다.")
        List<ReviewImageRegister> reviewImageRegisters
) {

    public RegisterReviewServiceRequest toServiceRequest() {
        return new RegisterReviewServiceRequest(orderId, serviceSatisfaction, satisfactionReason, description,
                reviewImageRegisters);
    }
}
