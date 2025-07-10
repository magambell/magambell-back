package com.magambell.server.review.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.order.app.port.out.OrderQueryPort;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.model.OrderGoods;
import com.magambell.server.review.app.port.in.ReviewUseCase;
import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewMyServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import com.magambell.server.review.app.port.out.ReviewCommandPort;
import com.magambell.server.review.app.port.out.ReviewQueryPort;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRatingSummaryDTO;
import com.magambell.server.review.app.port.out.response.ReviewRegisterResponseDTO;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
        OrderGoods orderGoods = orderQueryPort.findOrderGoodsById(request.orderGoodsId());

        validateOrderStatus(orderGoods);
        existsOrderReview(orderGoods, user);

        return reviewCommandPort.registerReview(request.toDto(user, orderGoods));
    }

    @Override
    public List<ReviewListDTO> getReviewList(final ReviewListServiceRequest request) {
        return reviewQueryPort.getReviewList(request, PageRequest.of(request.page() - 1, request.size()));
    }

    @Override
    public ReviewRatingSummaryDTO getReviewRatingAll(final ReviewRatingAllServiceRequest request) {
        return reviewQueryPort.getReviewRatingAll(request);
    }

    @Override
    public List<ReviewListDTO> getReviewListByUser(final ReviewMyServiceRequest request) {
        User user = userQueryPort.findById(request.userId());
        return reviewQueryPort.getReviewListByUser(user, PageRequest.of(request.page() - 1, request.size()));
    }

    private void validateOrderStatus(final OrderGoods orderGoods) {
        if (orderGoods.getOrder().getOrderStatus() != OrderStatus.COMPLETED) {
            throw new InvalidRequestException(ErrorCode.INVALID_ORDER_STATUS_COMPLETED);
        }
    }

    private void existsOrderReview(final OrderGoods orderGoods, final User user) {
        if (reviewQueryPort.existsOrderAndReview(orderGoods, user)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_REVIEW);
        }
    }
}
