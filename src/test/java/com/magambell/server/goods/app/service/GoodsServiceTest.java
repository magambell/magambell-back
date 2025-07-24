package com.magambell.server.goods.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.app.port.in.request.ChangeGoodsStatusServiceRequest;
import com.magambell.server.goods.app.port.in.request.EditGoodsServiceRequest;
import com.magambell.server.goods.app.port.in.request.RegisterGoodsServiceRequest;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.notification.infra.FirebaseNotificationSender;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSocialAccountRepository userSocialAccountRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @MockBean
    private FirebaseNotificationSender firebaseNotificationSender;

    private User user;
    private Store store;

    @BeforeEach
    void setUp() {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.OWNER);
        user = userSocialAccountDTO.toUser();
        user.addUserSocialAccount(userSocialAccountDTO.toUserSocialAccount());

        RegisterStoreDTO registerStoreDTO = new RegisterStoreDTO(
                "테스트 매장",
                "서울 강서구 테스트 211",
                1238.123213,
                5457.123213,
                "대표이름",
                "01012345678",
                "123491923",
                Bank.KB국민,
                "102391485",
                List.of(),
                Approved.APPROVED,
                user
        );
        store = registerStoreDTO.toEntity();
        user.addStore(store);
        user = userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        stockHistoryRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
        goodsRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userSocialAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("정상적으로 상품 등록이 된다")
    @Test
    void registerGoods() {
        // given
        RegisterGoodsServiceRequest req = new RegisterGoodsServiceRequest(
                "상품설명",
                LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 18, 0),
                3, 10000, 10, 9000
        );

        // when
        goodsService.registerGoods(req, user.getId());

        // then
        Goods goods = goodsRepository.findAll().get(0);

        assertThat(goods.getStock()).isNotNull();
        assertThat(goods.getStockQuantity()).isEqualTo(3);
        assertThat(stockHistoryRepository.findAll()).hasSize(1);
        assertThat(stockHistoryRepository.findAll().get(0).getResultQuantity()).isEqualTo(3);
    }

    @DisplayName("정상적으로 상품이 수정된다.")
    @Test
    void editGoods() {
        // given
        RegisterGoodsDTO dto = new RegisterGoodsDTO(
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 18, 0),
                3, 10000, 10, 9000,
                "상품설명",
                store);
        Store store = dto.store();
        Goods dtoGoods = dto.toGoods();

        store.addGoods(dtoGoods);
        Goods saveGoods = goodsRepository.save(dtoGoods);

        EditGoodsServiceRequest request = new EditGoodsServiceRequest(saveGoods.getId(), "이름 수정",
                "상품 설명2",
                LocalDateTime.of(2025, 1, 13, 9, 0),
                LocalDateTime.of(2025, 1, 13, 18, 0),
                5, 20000, 10, 16000, user.getId()
        );

        // when
        goodsService.editGoods(request);

        // then
        Goods goods = goodsRepository.findAll().get(0);

        assertThat(goods).extracting("name", "description", "startTime", "endTime", "originalPrice", "discount",
                        "salePrice")
                .contains(
                        "이름 수정",
                        "상품 설명2",
                        LocalDateTime.of(2025, 1, 13, 9, 0),
                        LocalDateTime.of(2025, 1, 13, 18, 0),
                        20000,
                        10,
                        16000
                );
        assertThat(goods.getStock()).isNotNull();
        assertThat(goods.getStockQuantity()).isEqualTo(5);
        assertThat(stockHistoryRepository.findAll()).hasSize(2);
        assertThat(stockHistoryRepository.findAll().get(1).getResultQuantity()).isEqualTo(5);
    }

    @DisplayName("사장님 판매 상태 변경")
    @Test
    void changeGoodsStatus() throws FirebaseMessagingException {
        // given
        RegisterGoodsDTO dto = new RegisterGoodsDTO(
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 18, 0),
                3, 10000, 10, 9000,
                "상품설명",
                store);
        Store store = dto.store();
        Goods dtoGoods = dto.toGoods();

        store.addGoods(dtoGoods);
        Goods saveGoods = goodsRepository.save(dtoGoods);
        ChangeGoodsStatusServiceRequest request = new ChangeGoodsStatusServiceRequest(
                saveGoods.getId(), SaleStatus.ON, user.getId());

        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 19, 0);

        // when
        doNothing().when(firebaseNotificationSender)
                .send("testToken", "테스트 매장", "테스트 매장");
        goodsService.changeGoodsStatus(request, localDateTime);

        // then
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(1);
        assertThat(goodsList.get(0).getSaleStatus()).isEqualTo(SaleStatus.ON);
        assertThat(goodsList.get(0).getStartTime()).isEqualTo(LocalDateTime.of(2025, 1, 2, 9, 0));
    }

    @DisplayName("자동 판매 종료")
    @Test
    void changeSaleStatusToOff() {
        // given
        RegisterGoodsDTO dto = new RegisterGoodsDTO(
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 18, 0),
                3, 10000, 10, 9000,
                "상품설명",
                store);
        Store store = dto.store();
        Goods dtoGoods = dto.toGoods();

        store.addGoods(dtoGoods);
        Goods saveGoods = goodsRepository.save(dtoGoods);
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 19, 0);

        // when
        goodsService.changeSaleStatusToOff(localDateTime);

        // then
        List<Goods> goodsList = goodsRepository.findAll();
        assertThat(goodsList).hasSize(1);
        assertThat(goodsList.get(0).getSaleStatus()).isEqualTo(SaleStatus.OFF);
    }
}