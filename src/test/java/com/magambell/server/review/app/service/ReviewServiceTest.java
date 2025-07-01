package com.magambell.server.review.app.service;


import static com.magambell.server.review.domain.enums.SatisfactionReason.AFFORDABLE;
import static com.magambell.server.review.domain.enums.SatisfactionReason.FRIENDLY;
import static com.magambell.server.review.domain.enums.SatisfactionReason.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.order.domain.repository.OrderGoodsRepository;
import com.magambell.server.order.domain.repository.OrderRepository;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import com.magambell.server.review.app.port.in.dto.RegisterReviewDTO;
import com.magambell.server.review.app.port.in.request.RegisterReviewServiceRequest;
import com.magambell.server.review.app.port.in.request.ReviewListServiceRequest;
import com.magambell.server.review.app.port.out.response.ReviewListDTO;
import com.magambell.server.review.domain.model.Review;
import com.magambell.server.review.domain.repository.ReviewImageRepository;
import com.magambell.server.review.domain.repository.ReviewReasonRepository;
import com.magambell.server.review.domain.repository.ReviewRepository;
import com.magambell.server.stock.domain.repository.StockHistoryRepository;
import com.magambell.server.stock.domain.repository.StockRepository;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.store.domain.repository.StoreImageRepository;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.repository.UserRepository;
import com.magambell.server.user.domain.repository.UserSocialAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
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
                Bank.IBK기업은행,
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
                order.getId(),
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

    @DisplayName("리뷰를 리스트를 출력한다.")
    @Test
    void getReviewList() {
        // given
        RegisterReviewDTO dto = new RegisterReviewDTO(
                order.getId(),
                2,
                List.of(FRIENDLY, AFFORDABLE, ZERO),
                "test",
                List.of(),
                user,
                order
        );
        reviewRepository.save(Review.create(dto));
        ReviewListServiceRequest request = new ReviewListServiceRequest(goods.getId(), false, 1, 10);

        // when
        List<ReviewListDTO> reviewList = reviewService.getReviewList(request);

        // then
        assertThat(reviewList).isNotNull();
        assertThat(reviewList.get(0).satisfactionReasons().get(0)).isEqualTo(FRIENDLY);
        assertThat(reviewList.get(0).description()).isEqualTo("test");
        assertThat(reviewList.get(0).goodsId()).isEqualTo(goods.getId());
    }
}