package com.magambell.server.favorite.app.service;

import static com.magambell.server.goods.domain.enums.SaleStatus.ON;
import static org.assertj.core.api.Assertions.assertThat;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.favorite.app.port.in.request.FavoriteStoreListServiceRequest;
import com.magambell.server.favorite.app.port.out.response.FavoriteStoreListDTOResponse;
import com.magambell.server.favorite.domain.entity.Favorite;
import com.magambell.server.favorite.domain.repository.FavoriteRepository;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.goods.app.port.in.dto.RegisterGoodsDTO;
import com.magambell.server.goods.domain.entity.Goods;
import com.magambell.server.goods.domain.repository.GoodsRepository;
import com.magambell.server.stock.domain.entity.Stock;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSocialAccountRepository userSocialAccountRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private StoreImageRepository storeImageRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;
    @Autowired
    private StockRepository stockRepository;

    private Store store;
    private User owner;

    @BeforeEach
    void setUp() {
        UserSocialAccountDTO ownerAccountDTO = new UserSocialAccountDTO(
                "test@test.com", "사장님", "사장님닉네임", "01077771111",
                ProviderType.KAKAO,
                "123974",
                UserRole.OWNER
        );
        owner = ownerAccountDTO.toUser();
        owner.addUserSocialAccount(ownerAccountDTO.toUserSocialAccount());

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
                owner,
                "주차장"
        );
        store = registerStoreDTO.toEntity();
        owner.addStore(store);
        owner = userRepository.save(owner);
    }

    @AfterEach
    void tearDown() {
        favoriteRepository.deleteAllInBatch();
        stockHistoryRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
        goodsRepository.deleteAllInBatch();
        storeImageRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userSocialAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("고객 즐겨찾기 매장 등록")
    @Test
    void registerFavorite() {
        // given
        UserSocialAccountDTO accountDTO = new UserSocialAccountDTO(
                "order@test.com", "주문자", "주문자닉", "01011112222",
                ProviderType.KAKAO,
                "socialId", UserRole.CUSTOMER
        );
        User user = accountDTO.toUser();
        user.addUserSocialAccount(accountDTO.toUserSocialAccount());
        user = userRepository.save(user);

        // when
        favoriteService.registerFavorite(store.getId(), user.getId());

        // then
        Favorite favorite = favoriteRepository.findAll().get(0);
        assertThat(favorite).isNotNull();
        assertThat(favorite.getStore().getId()).isEqualTo(store.getId());
        assertThat(favorite.getUser().getId()).isEqualTo(user.getId());
    }

    @DisplayName("고객 즐겨찾기 매장 삭제")
    @Test
    void deleteFavorite() {
        // given
        UserSocialAccountDTO accountDTO = new UserSocialAccountDTO(
                "order@test.com", "주문자", "주문자닉", "01011112222",
                ProviderType.KAKAO,
                "socialId", UserRole.CUSTOMER
        );
        User user = accountDTO.toUser();
        user.addUserSocialAccount(accountDTO.toUserSocialAccount());
        user = userRepository.save(user);

        Favorite saveFavorite = Favorite.create(store, user);
        favoriteRepository.save(saveFavorite);

        // when
        favoriteService.deleteFavorite(store.getId(), user.getId());

        // then
        List<Favorite> favorites = favoriteRepository.findAll();
        assertThat(favorites).isEmpty();
    }

    @DisplayName("고객 즐겨찾기 매장 리스트")
    @Test
    void getFavoriteStoreList() {
        // given
        Store store1 = createStore(1);
        Store store2 = createStore(2);

        UserSocialAccountDTO accountDTO = new UserSocialAccountDTO(
                "order@test.com", "주문자", "주문자닉", "01011112222",
                ProviderType.KAKAO,
                "socialId", UserRole.CUSTOMER
        );
        User user = accountDTO.toUser();
        user.addUserSocialAccount(accountDTO.toUserSocialAccount());
        user = userRepository.save(user);

        Favorite saveFavorite = Favorite.create(store1, user);
        favoriteRepository.save(saveFavorite);

        Favorite saveFavorite2 = Favorite.create(store2, user);
        favoriteRepository.save(saveFavorite2);

        FavoriteStoreListServiceRequest request = new FavoriteStoreListServiceRequest(1, 10,
                user.getId());

        // when
        List<FavoriteStoreListDTOResponse> favoriteStoreList = favoriteService.getFavoriteStoreList(request);

        // then
        assertThat(favoriteStoreList).hasSize(2);
        assertThat(favoriteStoreList)
                .extracting("storeName")
                .containsExactlyInAnyOrder("테스트 매장2", "테스트 매장1");
    }

    private Store createStore(int i) {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test" + i + "@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.OWNER);
        owner = userSocialAccountDTO.toUser();
        owner.addUserSocialAccount(userSocialAccountDTO.toUserSocialAccount());

        RegisterStoreDTO registerStoreDTO = new RegisterStoreDTO(
                "테스트 매장" + i,
                "서울 강서구 테스트 211",
                37.5665,
                37.5665,
                "대표이름",
                "01012345678",
                "123491923",
                Bank.KB국민,
                "102391485",
                List.of(),
                Approved.APPROVED,
                owner,
                "주차장"
        );

        Store store = registerStoreDTO.toEntity();

        RegisterGoodsDTO registerGoodsDTO = new RegisterGoodsDTO(
                LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 18, 0),
                i, 10000, 10, 9000, store,
                List.of(new GoodsImagesRegister(0, "test", "상품명"))
        );
        owner.addStore(store);

        Goods goods = registerGoodsDTO.toGoods();
        store.addGoods(goods);
        Stock stock = Stock.create(registerGoodsDTO.quantity());
        goods.addStock(stock);

        userRepository.save(owner);
        goods.changeStatus(owner, ON, LocalDateTime.of(2025, 1, 1, 8, 0));
        return store;
    }
}