package com.magambell.server.store.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.store.adapter.out.persistence.StoreAdminListResponse;
import com.magambell.server.store.adapter.out.persistence.StoreDetailResponse;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.adapter.out.persistence.StoreListResponse;
import com.magambell.server.store.app.port.in.StoreUseCase;
import com.magambell.server.store.app.port.in.request.CloseStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.app.port.in.request.StoreApproveServiceRequest;
import com.magambell.server.store.app.port.in.request.WaitingStoreListServiceRequest;
import com.magambell.server.store.app.port.out.StoreCommandPort;
import com.magambell.server.store.app.port.out.StoreQueryPort;
import com.magambell.server.store.app.port.out.response.OwnerStoreDetailDTO;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import com.magambell.server.store.app.port.out.response.StoreRegisterResponseDTO;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
        StoreRegisterResponseDTO storeRegisterResponseDTO = storeCommandPort.registerStore(
                request.toStoreDTO(Approved.WAITING, user));

        return new StoreImagesResponse(String.valueOf(storeRegisterResponseDTO.id()),
                storeRegisterResponseDTO.storePreSignedUrlImages());
    }

    @Override
    public StoreListResponse getStoreList(final SearchStoreListServiceRequest request) {
        return new StoreListResponse(
                storeQueryPort.getStoreList(request, PageRequest.of(request.page() - 1, request.size())));
    }

    @Transactional
    @Override
    public void storeApprove(final StoreApproveServiceRequest request) {
        storeCommandPort.storeApprove(request.id());
    }

    @Override
    public StoreDetailResponse getStoreDetail(final Long storeId) {
        return storeQueryPort.getStoreDetail(storeId);
    }

    @Override
    public OwnerStoreDetailDTO getOwnerStoreInfo(final Long userId) {
        User user = userQueryPort.findById(userId);
        return storeQueryPort.getOwnerStoreInfo(user);
    }

    @Override
    public StoreListResponse getCloseStoreList(final CloseStoreListServiceRequest request) {
        return new StoreListResponse(storeQueryPort.getCloseStoreList(request));
    }

    @Override
    public StoreAdminListResponse getWaitingStoreList(final WaitingStoreListServiceRequest request) {
        return new StoreAdminListResponse(
                storeQueryPort.getWaitingStoreList(PageRequest.of(request.page() - 1, request.size())));
    }

    @Override
    public StoreImagesResponse getStoreImageList(final Long userId, final Long storeId) {
        User user = userQueryPort.findById(userId);
        validateUserRoleAndStore(user, storeId);

        List<StorePreSignedUrlImage> images =
                Optional.ofNullable(storeQueryPort.getStoreImageList(storeId))
                        .orElseGet(List::of).stream()
                        .filter(Objects::nonNull)
                        .map(img -> new StorePreSignedUrlImage(img.getOrder(), img.getName()))
                        .toList();

        return new StoreImagesResponse(String.valueOf(storeId), images);
    }

    private void checkDuplicateStore(final User user) {
        if (storeQueryPort.existsByUser(user)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_STORE);
        }
    }

    private void validateUserRoleAndStore(final User user, final Long storeId) {
        if (user.getUserRole() == UserRole.ADMIN) {
            return;
        }

        Long ownerId = storeQueryPort.findOwnerIdByStoreId(storeId);

        if (!ownerId.equals(user.getId())) {
            throw new InvalidRequestException(ErrorCode.INVALID_STORE_OWNER);
        }
    }
}
