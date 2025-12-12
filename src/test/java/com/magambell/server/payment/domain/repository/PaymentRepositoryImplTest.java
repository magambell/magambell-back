package com.magambell.server.payment.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.order.app.port.in.dto.CreateOrderDTO;
import com.magambell.server.order.domain.entity.Order;
import com.magambell.server.order.domain.repository.OrderGoodsRepository;
import com.magambell.server.order.domain.repository.OrderRepository;
import com.magambell.server.payment.app.port.in.dto.CreatePaymentDTO;
import com.magambell.server.payment.domain.entity.Payment;
import com.magambell.server.payment.domain.enums.PaymentStatus;
import com.magambell.server.stock.domain.repository.StockHistoryRepository;
import com.magambell.server.stock.domain.repository.StockRepository;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.entity.User;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.repository.UserRepository;
import com.magambell.server.user.domain.repository.UserSocialAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@ActiveProfiles("test")
@SpringBootTest
class PaymentRepositoryImplTest {

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
    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager em;

    private User user;
    private Goods goods;
    private User owner;
    private Order saveOrder;

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
                owner,
                null,
                "주차장");
        Store store = registerStoreDTO.toEntity();

        // 상품 생성
        RegisterGoodsDTO registerGoodsDTO = new RegisterGoodsDTO(
                "상품명",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(2),
                120, 10000, 10, 9000,
                store,
                List.of(new GoodsImagesRegister(0, "test", "https://test.com/test.jpg", "상품명"))
        );
        goods = Goods.create(registerGoodsDTO);
        store.addGoods(goods);

        owner.addStore(store);
        userRepository.save(owner);

        CreateOrderDTO createOrderDTO = new CreateOrderDTO(user, goods, 1, 9000, LocalDateTime.now(), "test");
        Order createOrder = createOrderDTO.toOrder();
        createOrder.completed();
        saveOrder = orderRepository.save(createOrder);

        Payment savePayment = new CreatePaymentDTO(createOrder, 9000, PaymentStatus.PAID).toPayment();
        paymentRepository.save(savePayment);
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

    @DisplayName("payment 영속성으로 두고 다른 도메인 호출시 N+1 호출 이슈 체크")
    @Transactional
    @Test
    void testFindByMerchantUidWithLockAndRelationsNoNPlusOne() {
        // given
        Session session = em.unwrap(Session.class);
        session.getSessionFactory().getStatistics().clear();
        session.getSessionFactory().getStatistics().setStatisticsEnabled(true);
        Statistics stats = session.getSessionFactory().getStatistics();

        // when
        String merchantUid = "ORDER_" + saveOrder.getId();
        Payment payment = paymentRepository.findByMerchantUidWithLockAndRelations(merchantUid)
                .orElseThrow();

        // then
        payment.getOrder().getOrderGoodsList().forEach(og -> {
            og.getGoods().getStock().getQuantity();
            og.getGoods().getStore().getUser().getName();
        });

        long queryCount = stats.getQueryExecutionCount();
        // 2개의 메인 쿼리 + 환경에 따른 추가 쿼리 (최대 3개)
        assertThat(queryCount).isLessThanOrEqualTo(3);
    }

    @DisplayName("Locked 제대로 되는지 체크")
    @Test
    void testPessimisticLockBlocksOtherTransaction() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        String merchantUid = "ORDER_" + saveOrder.getId();

        CountDownLatch latch = new CountDownLatch(1);

        Future<Void> tx1 = executor.submit(() -> {
            TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
            txTemplate.execute(status -> {
                Payment p = paymentRepository.findByMerchantUidWithLockAndRelations(merchantUid).orElseThrow();
                latch.countDown();
                try {
                    Thread.sleep(3000); // 락 유지
                } catch (InterruptedException e) {
                }
                return null;
            });
            return null;
        });

        Future<Void> tx2 = executor.submit(() -> {
            latch.await();
            TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
            txTemplate.setTimeout(1);

            assertThatThrownBy(() ->
                    txTemplate.execute(status -> {
                        paymentRepository.findByMerchantUidWithLockAndRelations(merchantUid).orElseThrow();
                        return null;
                    })
            ).isInstanceOf(Exception.class);

            return null;
        });

        tx1.get();
        tx2.get();
    }

}
