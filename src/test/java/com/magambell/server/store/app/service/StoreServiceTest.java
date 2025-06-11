package com.magambell.server.store.app.service;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.model.Store;
import com.magambell.server.store.domain.repository.StoreRepository;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.repository.UserRepository;
import com.magambell.server.user.domain.repository.UserSocialAccountRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
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

    private User user;

    @BeforeEach
    void setUp() {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.CUSTOMER);
        user = userRepository.save(userSocialAccountDTO.toUser());
        userSocialAccountRepository.save(userSocialAccountDTO.toUserSocialAccount());
    }

    @DisplayName("매장을 등록한다.")
    @Test
    void registerStore() {
        // given
        RegisterStoreServiceRequest request = new RegisterStoreServiceRequest(
                "테스트 매장",
                "서울 강서구 테스트 211",
                "1238.123213",
                "5457.123213",
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
}