package com.magambell.server.order.app.service;

import static com.magambell.server.payment.app.service.PaymentService.MERCHANT_UID_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.model.Order;
import com.magambell.server.order.domain.repository.OrderGoodsRepository;
import com.magambell.server.order.domain.repository.OrderRepository;
import com.magambell.server.payment.domain.model.Payment;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import com.magambell.server.stock.domain.repository.StockHistoryRepository;
import com.magambell.server.stock.domain.repository.StockRepository;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.model.Store;
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
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSocialAccountRepository userSocialAccountRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private OrderGoodsRepository orderGoodsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    private User user;
    private Goods goods;

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

    }

    @AfterEach
    void tearDown() {
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

    @DisplayName("정상적으로 주문이 생성되고, 재고가 차감된다.")
    @Test
    void createOrderSuccess() {
        // given
        CreateOrderServiceRequest request = new CreateOrderServiceRequest(
                goods.getId(),
                2,
                18000,
                LocalDateTime.now().plusMinutes(30),
                "빨리 주세요"
        );

        // when
        orderService.createOrder(request, user.getId());

        // then
        Goods updatedGoods = goodsRepository.findById(goods.getId()).orElse(null);
        assertThat(updatedGoods).isNotNull();
        assertThat(updatedGoods.getStock().getQuantity()).isEqualTo(8);

        Order order = orderRepository.findAll().get(0);
        Payment payment = paymentRepository.findAll().get(0);
        assertThat(order).isNotNull();
        assertThat(order.getUser().getId()).isEqualTo(user.getId());
        assertThat(order.getUser().getId()).isEqualTo(user.getId());
        assertThat(payment).isNotNull();
        assertThat(payment.getMerchantUid()).isEqualTo(MERCHANT_UID_PREFIX + order.getId().toString());
    }

    @DisplayName("고객 주문 목록")
    @Test
    void getOrderList() {
        // given
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.now(), "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.completed();
        orderRepository.save(createOrder);

        // when
        List<OrderListDTO> orderList = orderService.getOrderList(user.getId());

        // then
        assertThat(orderList.size()).isEqualTo(1);
        OrderListDTO orderListDTO = orderList.get(0);
        assertThat(orderListDTO.orderStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(orderListDTO.salePrice()).isEqualTo(9000);
    }
}