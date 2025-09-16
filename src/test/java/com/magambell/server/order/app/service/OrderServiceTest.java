package com.magambell.server.order.app.service;

import static com.magambell.server.order.domain.enums.OrderStatus.COMPLETED;
import static com.magambell.server.order.domain.enums.OrderStatus.PAID;
import static com.magambell.server.payment.app.service.PaymentService.MERCHANT_UID_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.notification.infra.FirebaseNotificationSender;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.app.port.in.request.CreateOrderServiceRequest;
import com.magambell.server.order.app.port.in.request.CustomerOrderListServiceRequest;
import com.magambell.server.order.app.port.in.request.OwnerOrderListServiceRequest;
import com.magambell.server.order.app.port.in.request.RejectOrderServiceRequest;
import com.magambell.server.order.app.port.out.response.OrderDetailDTO;
import com.magambell.server.order.app.port.out.response.OrderListDTO;
import com.magambell.server.order.app.port.out.response.OrderStoreListDTO;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.enums.OrderStatus;
import com.magambell.server.order.domain.enums.RejectReason;
import com.magambell.server.order.domain.repository.OrderGoodsRepository;
import com.magambell.server.order.domain.repository.OrderRepository;
import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.app.port.out.PortOnePort;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import com.magambell.server.payment.domain.repository.PaymentRepository;
import com.magambell.server.stock.domain.entity.Stock;
import com.magambell.server.stock.domain.repository.StockHistoryRepository;
import com.magambell.server.stock.domain.repository.StockRepository;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.entity.Store;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
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
    @MockBean
    private PortOnePort portOnePort;
    @MockBean
    private FirebaseNotificationSender firebaseNotificationSender;
    private User user;
    private Goods goods;
    private User owner;

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
        owner = ownerAccountDTO.toUser();
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
                120, 10000, 10, 9000, "",
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
        orderService.createOrder(request, user.getId(), LocalDateTime.now().plusMinutes(10));

        // then
        Goods updatedGoods = goodsRepository.findById(goods.getId()).orElse(null);
        assertThat(updatedGoods).isNotNull();
        assertThat(updatedGoods.getStock().getQuantity()).isEqualTo(118);

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

        CreateOrderDTO createOrderDTO2 = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.now(), "test");
        Order createOrder2 = createOrderDTO2.toOrder();
        createOrder2.paid();
        orderRepository.save(createOrder2);

        CustomerOrderListServiceRequest pageRequest = new CustomerOrderListServiceRequest(1, 10);

        // when
        List<OrderListDTO> orderList = orderService.getOrderList(pageRequest, user.getId());

        // then
        assertThat(orderList.size()).isEqualTo(2);
        OrderListDTO orderListDTO = orderList.get(0);
        assertThat(orderListDTO.orderStatus()).isEqualTo(PAID);
        assertThat(orderListDTO.goodsList().get(0).salePrice()).isEqualTo(9000);
    }

    @DisplayName("고객 주문상세")
    @Test
    void getOrderDetail() {
        // given
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.now(), "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.completed();
        orderRepository.save(createOrder);

        Payment payment = new CreatePaymentDTO(createOrder, 9000, PaymentStatus.PAID).toPayment();
        paymentRepository.save(payment);

        // when
        OrderDetailDTO orderDetail = orderService.getOrderDetail(createOrder.getId(), user.getId());

        // then
        assertThat(orderDetail).isNotNull();
        assertThat(orderDetail.orderStatus()).isEqualTo(COMPLETED);
        assertThat(orderDetail.totalPrice()).isEqualTo(9000);
    }

    @DisplayName("사장님 전체 주문 목록")
    @Test
    void getOrderStoreList() {
        // given
        List<Order> orderList = IntStream.range(1, 31)
                .mapToObj(this::createOrder)
                .toList();
        orderRepository.saveAll(orderList);
        OwnerOrderListServiceRequest request = new OwnerOrderListServiceRequest(1, 10, null);

        // when
        List<OrderStoreListDTO> orderStoreList = orderService.getOrderStoreList(request, owner.getId());

        // then
        assertThat(orderStoreList.size()).isEqualTo(10);
        assertThat(orderStoreList.get(0)).extracting("orderStatus", "pickupTime", "quantity", "totalPrice")
                .contains(
                        COMPLETED,
                        LocalDateTime.of(2025, 6, 30, 17, 30),
                        30,
                        9000
                );
    }

    @DisplayName("사장님 주문 목록 - 대기(PAID)")
    @Test
    void getOrderStoreListByStatusPaid() {
        // given
        List<Order> orderList = IntStream.range(1, 31)
                .mapToObj(this::createOrderPaid)
                .toList();
        orderRepository.saveAll(orderList);
        OwnerOrderListServiceRequest request = new OwnerOrderListServiceRequest(1, 10, PAID);

        // when
        List<OrderStoreListDTO> orderStoreList = orderService.getOrderStoreList(request, owner.getId());

        // then
        assertThat(orderStoreList.size()).isEqualTo(10);
        assertThat(orderStoreList.get(0)).extracting("orderStatus", "pickupTime", "quantity", "totalPrice")
                .contains(
                        PAID,
                        LocalDateTime.of(2025, 6, 30, 17, 30),
                        30,
                        9000
                );
    }

    @DisplayName("사장님이 주문을 승인하면 상태가 ACCEPTED로 변경된다.")
    @Test
    void approveOrder() throws FirebaseMessagingException {
        // given
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.of(2025, 6, 30, 17, 30),
                "test");
        Order order = createOrderDTO.toOrder();
        order.paid();
        orderRepository.save(order);
        CreatePaymentDTO createPaymentDTO = new CreatePaymentDTO(order, order.getTotalPrice(), PaymentStatus.PAID);
        Payment payment = createPaymentDTO.toPayment();
        paymentRepository.save(payment);

        // when
        doNothing().when(firebaseNotificationSender)
                .send("testToken", "테스트 매장", "테스트 매장");
        orderService.approveOrder(order.getId(), owner.getId(), LocalDateTime.of(2025, 6, 30, 13, 30));

        // then
        Order result = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @DisplayName("사장님이 주문을 거절하면 상태가 REJECTED로 변경되고 재고가 복구되며 결제 취소 요청이 호출된다.")
    @Test
    void rejectOrder() throws FirebaseMessagingException {
        // given
        CreateOrderServiceRequest createRequest = new CreateOrderServiceRequest(
                goods.getId(),
                2,
                18000,
                LocalDateTime.now().plusMinutes(30),
                "빨리 주세요"
        );
        orderService.createOrder(createRequest, user.getId(), LocalDateTime.now().plusMinutes(10));

        Order order = orderRepository.findAll().get(0);
        order.paid();
        orderRepository.save(order);

        RejectOrderServiceRequest request = new RejectOrderServiceRequest(order.getId(),
                owner.getId(), RejectReason.STORE_ISSUE);

        // when
        doNothing().when(firebaseNotificationSender)
                .send("testToken", "테스트 매장", "테스트 매장");
        orderService.rejectOrder(request);

        verify(portOnePort, times(1))
                .cancelPayment(eq(order.getPayment().getMerchantUid()), eq(18000), eq("사장님 주문 취소"));

        // then
        Order result = orderRepository.findById(order.getId()).orElseThrow();
        Stock updatedStock = stockRepository.findAll().get(0);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.REJECTED);
        assertThat(updatedStock.getQuantity()).isEqualTo(120);
        assertThat(result.getRejectReason()).isEqualTo(RejectReason.STORE_ISSUE);
    }

    @DisplayName("고객이 주문을 취소하면 재고가 복구되며 결제 취소 요청이 호출된다.")
    @Test
    void cancelOrder() {
        // given
        CreateOrderServiceRequest request = new CreateOrderServiceRequest(
                goods.getId(),
                2,
                18000,
                LocalDateTime.now().plusMinutes(30),
                "빨리 주세요"
        );
        orderService.createOrder(request, user.getId(), LocalDateTime.now().plusMinutes(10));

        Order order = orderRepository.findAll().get(0);
        order.paid();
        orderRepository.save(order);

        // when
        orderService.cancelOrder(order.getId(), user.getId());

        // then
        Order result = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);

        Stock updatedStock = stockRepository.findAll().get(0);
        assertThat(updatedStock.getQuantity()).isEqualTo(120);

        verify(portOnePort, times(1))
                .cancelPayment(eq(order.getPayment().getMerchantUid()), eq(18000), eq("고객님 주문 취소"));
    }

    @DisplayName("주문을 승인하면 상태가 ACCEPTED로 변경된다.")
    @Test
    void completedOrder() {
        // given
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.of(2025, 6, 30, 17, 30),
                "test");
        Order order = createOrderDTO.toOrder();
        order.accepted();
        orderRepository.save(order);
        CreatePaymentDTO createPaymentDTO = new CreatePaymentDTO(order, order.getTotalPrice(), PaymentStatus.PAID);
        Payment payment = createPaymentDTO.toPayment();
        paymentRepository.save(payment);

        // when
        orderService.completedOrder(order.getId(), owner.getId());

        // then
        Order result = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(result.getOrderStatus()).isEqualTo(COMPLETED);
    }

    private Order createOrder(int i) {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, i, 9000, LocalDateTime.of(2025, 6, 30, 17, 30),
                "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.completed();
        return createOrder;
    }

    private Order createOrderPaid(int i) {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, i, 9000, LocalDateTime.of(2025, 6, 30, 17, 30),
                "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.paid();
        return createOrder;
    }
}