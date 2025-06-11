package com.magambell.server.goods.app.service;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.goods.domain.repository.GoodsRepository;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    private User user;
    private Store store;

    @BeforeEach
    void setUp() {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.CUSTOMER);
        user = userRepository.save(userSocialAccountDTO.toUser());
        userSocialAccountRepository.save(userSocialAccountDTO.toUserSocialAccount());

        storeRepository.save(new RegisterStoreDTO(
                        "테스트 매장",
                        "서울 강서구 테스트 211",
                        "1238.123213",
                        "5457.123213",
                        "대표이름",
                        "01012345678",
                        "123491923",
                        Bank.IBK기업은행,
                        "102391485",
                        List.of(),
                        Approved.WAITING
                ).toEntity()
        );
    }

    @DisplayName("정상적으로 상품 등록이 된다")
    @Test
    void registerGoods_success() {
        // given
//        RegisterGoodsServiceRequest req = new RegisterGoodsServiceRequest(
//                "상품명", "상품설명",
//                LocalTime.of(9, 0), LocalTime.of(18, 0),
//                3, 10000, 10, 9000
//        );
//
//        // when
//        goodsService.registerGoods(req, user.getId());
//
//        // then
//        Goods goods = goodsRepository.findAll().get(0);
//        assertThat(goods.getName()).isEqualTo("상품명");
    }
}