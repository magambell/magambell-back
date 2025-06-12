package com.magambell.server.store.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.store.adapter.in.web.SearchStoreListServiceRequest;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.StoreUseCase;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.app.port.out.StoreCommandPort;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.app.port.out.response.PreSignedUrlImage;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService implements StoreUseCase {

    private final StoreCommandPort storeCommandPort;
    private final StoreQueryPort storeQueryPort;
    private final UserQueryPort userQueryPort;

    @Transactional
    @Override
    public StoreImagesResponse registerStore(final RegisterStoreServiceRequest request, final Long userId) {
        User user = userQueryPort.findById(userId);
        checkDuplicateStore(user);
        List<PreSignedUrlImage> preSignedUrlImages = storeCommandPort.registerStore(
                request.toStoreDTO(Approved.WAITING, user));

        return new StoreImagesResponse(preSignedUrlImages);
    }

    @Override
    public StoreListResponse getStoreList(final SearchStoreListServiceRequest request) {
        return new StoreListResponse(storeQueryPort.getStoreList(request));
    }

    private void checkDuplicateStore(final User user) {
        if (storeQueryPort.existsByUser(user)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_STORE);
        }
    }
}
