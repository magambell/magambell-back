package com.magambell.server.store.app.service;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.model.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.stock.domain.model.Stock;
import com.magambell.server.stock.domain.repository.StockRepository;
import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.app.port.out.response.StoreListDTOResponse;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.enums.SearchSortType;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.store.domain.repository.StoreImageRepository;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.repository.UserRepository;
import com.magambell.server.user.domain.repository.UserSocialAccountRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class StoreServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSocialAccountRepository userSocialAccountRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StockRepository stockRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.OWNER);
        user = userSocialAccountDTO.toUser();
        user.addUserSocialAccount(userSocialAccountDTO.toUserSocialAccount());
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAllInBatch();
        goodsRepository.deleteAllInBatch();
        storeImageRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userSocialAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("매장을 등록한다.")
    @Test
    void registerStore() {
        // given
        RegisterStoreServiceRequest request = new RegisterStoreServiceRequest(
                "테스트 매장",
                "서울 강서구 테스트 211",
                1238.123213,
                5457.123213,
                "대표이름",
                "01012345678",
                "123491923",
                Bank.IBK기업은행,
                "102391485",
                List.of(new StoreImagesRegister(0, "test"))
        );

        // when
        storeService.registerStore(request, user.getId());

        // then
        Store store = storeRepository.findAll().get(0);
        Assertions.assertThat(store).extracting("name", "address", "ownerPhone")
                .contains(
                        "테스트 매장",
                        "서울 강서구 테스트 211",
                        "01012345678");
    }

    @DisplayName("매장 리스트를 가져온다.")
    @Test
    void getStoreList() {
        // given
        SearchStoreListServiceRequest request = new SearchStoreListServiceRequest(
                37.5665, 37.5665, "매장1", SearchSortType.DISTANCE_ASC, true
        );

        List<Store> storeList = IntStream.range(1, 31)
                .mapToObj(this::createStore)
                .toList();

        storeRepository.saveAll(storeList);

        // when
        StoreListResponse storeListResponse = storeService.getStoreList(request);

        // then
        StoreListDTOResponse store = storeListResponse.storeListDTOResponses().get(0);
        Assertions.assertThat(store)
                .extracting("storeName", "goodsName", "startTime", "endTime", "originPrice", "discount", "salePrice",
                        "quantity")
                .contains(
                        "테스트 매장1",
                        "상품명1",
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0),
                        10000,
                        10,
                        9000,
                        1
                );
    }

    private Store createStore(int i) {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test" + i + "@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.OWNER);
        user = userSocialAccountDTO.toUser();
        user.addUserSocialAccount(userSocialAccountDTO.toUserSocialAccount());

        RegisterStoreDTO registerStoreDTO = new RegisterStoreDTO(
                "테스트 매장" + i,
                "서울 강서구 테스트 211",
                37.5665,
                37.5665,
                "대표이름",
                "01012345678",
                "123491923",
                Bank.IBK기업은행,
                "102391485",
                List.of(),
                Approved.APPROVED,
                user
        );

        Store store = registerStoreDTO.toEntity();

        RegisterGoodsDTO registerGoodsDTO = new RegisterGoodsDTO(
                "상품명" + i,
                LocalTime.of(9, 0), LocalTime.of(18, 0),
                i, 10000, 10, 9000, "상품설명", store
        );
        user.addStore(store);

        Goods goods = registerGoodsDTO.toGoods();
        Stock stock = registerGoodsDTO.toStock();
        store.addGoods(goods);
        goods.addStock(stock);

        userRepository.save(user);
        return store;
    }
}