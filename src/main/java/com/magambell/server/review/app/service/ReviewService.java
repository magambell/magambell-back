package com.magambell.server.review.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.review.app.port.in.ReviewUseCase;
import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.app.port.out.ReviewCommandPort;
import com.magambell.server.review.app.port.out.ReviewQueryPort;
import com.magambell.server.review.app.port.out.response.ReviewRegisterResponseDTO;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService implements ReviewUseCase {

    private final ReviewCommandPort reviewCommandPort;
    private final ReviewQueryPort reviewQueryPort;
    private final UserQueryPort userQueryPort;
    private final OrderQueryPort orderQueryPort;

    @Transactional
    @Override
    public ReviewRegisterResponseDTO registerReview(final RegisterReviewServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        Order order = orderQueryPort.findById(request.orderId());

        validateOrderStatus(order);
        existsOrderReview(order, user);

        return reviewCommandPort.registerReview(request.toDto(user, order));
    }

    private void validateOrderStatus(final Order order) {
        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new InvalidRequestException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }

    private void existsOrderReview(final Order order, final User user) {
        if (reviewQueryPort.existsOrderAndReview(order, user)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_REVIEW);
        }
    }
}
