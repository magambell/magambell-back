package com.magambell.server.review.app.service;


import static com.magambell.server.review.domain.enums.SatisfactionReason.AFFORDABLE;
import static com.magambell.server.review.domain.enums.SatisfactionReason.FRIENDLY;
import static com.magambell.server.review.domain.enums.SatisfactionReason.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.entity.OrderGoods;
import com.magambell.server.order.domain.repository.OrderGoodsRepository;
import com.magambell.server.order.domain.repository.OrderRepository;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.app.port.in.request.DeleteReviewServiceRequest;
import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewMyServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewRatingAllServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.app.port.out.response.ReviewRatingSummaryDTO;
import com.magambell.server.review.domain.enums.ReviewStatus;
import com.magambell.server.review.domain.entity.Review;
import com.magambell.server.review.domain.repository.ReviewImageRepository;
import com.magambell.server.review.domain.repository.ReviewReasonRepository;
import com.magambell.server.review.domain.repository.ReviewRepository;
import com.magambell.server.stock.domain.repository.StockHistoryRepository;
import com.magambell.server.stock.domain.repository.StockRepository;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.repository.StoreImageRepository;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.entity.User;
import com.magambell.server.user.domain.repository.UserRepository;
import com.magambell.server.user.domain.repository.UserSocialAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSocialAccountRepository userSocialAccountRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreImageRepository storeImageRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewImageRepository reviewImageRepository;
    @Autowired
    private ReviewReasonRepository reviewReasonRepository;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderGoodsRepository orderGoodsRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    private User user;
    private Goods goods;
    private Order order;
    private OrderGoods orderGoods;

    @BeforeEach
    void setUp() {
        // 사용자 생성
        UserSocialAccountDTO accountDTO = new UserSocialAccountDTO(
                "order@test.com", "주문자", "주문자닉", "01011112222",
                ProviderType.KAKAO,
                "socialId", UserRole.CUSTOMER
        );
        user = accountDTO.toUser();
        user.addUserSocialAccount(accountDTO.toUserSocialAccount());
        user = userRepository.save(user);

        UserSocialAccountDTO ownerAccountDTO = new UserSocialAccountDTO(
                "test@test.com", "사장님", "사장님닉네임", "01077771111",
                ProviderType.KAKAO,
                "123974",
                UserRole.OWNER
        );
        User owner = ownerAccountDTO.toUser();
        owner.addUserSocialAccount(ownerAccountDTO.toUserSocialAccount());

        // 매장 생성
        RegisterStoreDTO registerStoreDTO = new RegisterStoreDTO(
                "테스트매장",
                "서울시",
                1.0, 2.0,
                "대표",
                "01099998888",
                "123123",
                Bank.KB국민,
                "9876543210",
                List.of(),
                Approved.APPROVED,
                owner);
        Store store = registerStoreDTO.toEntity();

        // 상품 생성
        RegisterGoodsDTO registerGoodsDTO = new RegisterGoodsDTO(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(2),
                10, 10000, 10, 9000, "",
                store
        );
        goods = Goods.create(registerGoodsDTO);
        store.addGoods(goods);

        owner.addStore(store);
        userRepository.save(owner);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.now(), "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.completed();
        order = orderRepository.save(createOrder);
        orderGoods = order.getOrderGoodsList().get(0);
    }

    @AfterEach
    void tearDown() {
        reviewReasonRepository.deleteAllInBatch();
        reviewImageRepository.deleteAllInBatch();
        reviewRepository.deleteAllInBatch();
        stockHistoryRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
        orderGoodsRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        goodsRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userSocialAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("리뷰를 등록한다.")
    @Test
    void registerReview() {
        // given
        RegisterReviewServiceRequest request = new RegisterReviewServiceRequest(
                orderGoods.getId(),
                2,
                List.of(FRIENDLY),
                "test",
                List.of()
        );

        // when
        reviewService.registerReview(request, user.getId());

        // then
        Review review = reviewRepository.findAll().get(0);
        assertThat(review).isNotNull();
        assertThat(review.getDescription()).isEqualTo("test");
    }

    @DisplayName("리뷰 리스트를 출력한다.")
    @Test
    void getReviewList() {
        // given
        RegisterReviewDTO dto = new RegisterReviewDTO(
                orderGoods.getId(),
                2,
                List.of(FRIENDLY, AFFORDABLE, ZERO),
                "test",
                List.of(),
                user,
                orderGoods
        );
        reviewRepository.save(Review.create(dto));
    ReviewListServiceRequest request = new ReviewListServiceRequest(goods.getId(), false, 1, 10);

        // when
        List<ReviewListDTO> reviewList = reviewService.getReviewList(request);

        // then
        assertThat(reviewList).isNotNull();
        assertThat(reviewList.get(0).satisfactionReasons()).contains(FRIENDLY);
        assertThat(reviewList.get(0).description()).isEqualTo("test");
        assertThat(reviewList.get(0).goodsId()).isEqualTo(goods.getId());
    }

    @DisplayName("리뷰를 리스트 평점별 조회")
    @Test
    void getReviewRatingAll() {
        // given
        List<Review> reviewList = IntStream.range(2, 6)
                .mapToObj(this::createReview)
                .toList();

        reviewRepository.saveAll(reviewList);

        List<Review> reviewList2 = IntStream.range(2, 4)
                .mapToObj(this::createReview)
                .toList();

        reviewRepository.saveAll(reviewList2);
        ReviewRatingAllServiceRequest request = new ReviewRatingAllServiceRequest(goods.getId(), false);

        // when
        ReviewRatingSummaryDTO reviewRatingAll = reviewService.getReviewRatingAll(request);

        // then
        assertThat(reviewRatingAll).isNotNull();
        assertThat(reviewRatingAll.averageRating()).isEqualTo(3.2);
        assertThat(reviewRatingAll.totalCount()).isEqualTo(6);
        assertThat(reviewRatingAll.rating2Count()).isEqualTo(2);
    }

    @DisplayName("내가 작성한 리뷰 목록")
    @Test
    void getReviewListByUser() {
        // given
        RegisterReviewDTO dto = new RegisterReviewDTO(
                order.getId(),
                2,
                List.of(FRIENDLY, AFFORDABLE, ZERO),
                "test",
                List.of(),
                user,
                orderGoods
        );
        reviewRepository.save(Review.create(dto));
        ReviewMyServiceRequest request = new ReviewMyServiceRequest(1, 10, user.getId());

        // when
        List<ReviewListDTO> reviewList = reviewService.getReviewListByUser(request);

        // then
        assertThat(reviewList).isNotNull();
        assertThat(reviewList.get(0).satisfactionReasons()).contains(FRIENDLY);
        assertThat(reviewList.get(0).description()).isEqualTo("test");
        assertThat(reviewList.get(0).nickName()).isEqualTo(user.getNickName());
    }

    @DisplayName("내가 작성한 리뷰 삭제")
    @Test
    void deleteReview() {
        // given
        RegisterReviewDTO dto = new RegisterReviewDTO(
                order.getId(),
                2,
                List.of(FRIENDLY, AFFORDABLE, ZERO),
                "test",
                List.of(),
                user,
                orderGoods
        );
        Review review = reviewRepository.save(Review.create(dto));

        // when
        reviewService.deleteReview(new DeleteReviewServiceRequest(review.getId(), user.getId()));

        // then
        List<Review> reviewAll = reviewRepository.findAll();
        assertThat(reviewAll).hasSize(1);
        assertThat(reviewAll.get(0).getReviewStatus()).isEqualTo(ReviewStatus.DELETED);
    }

    private Review createReview(int i) {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.now(), "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.completed();
        order = orderRepository.save(createOrder);
        orderGoods = order.getOrderGoodsList().get(0);

        RegisterReviewDTO dto = new RegisterReviewDTO(
                order.getOrderGoodsList().get(0).getId(),
                i,
                List.of(FRIENDLY, AFFORDABLE, ZERO),
                "test",
                List.of(),
                user,
                orderGoods
        );

        return Review.create(dto);
    }
}