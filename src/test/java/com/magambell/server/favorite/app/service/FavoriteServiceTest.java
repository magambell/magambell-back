package com.magambell.server.favorite.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.favorite.domain.model.Favorite;
import com.magambell.server.favorite.domain.repository.FavoriteRepository;
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
                Bank.IBK기업은행,
                "102391485",
                List.of(),
                Approved.APPROVED,
                owner
        );
        store = registerStoreDTO.toEntity();
        owner.addStore(store);
        owner = userRepository.save(owner);
    }

    @AfterEach
    void tearDown() {
        favoriteRepository.deleteAllInBatch();
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
}