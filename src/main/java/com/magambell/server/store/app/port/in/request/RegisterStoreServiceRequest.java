package com.magambell.server.store.app.port.in.request;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.user.domain.entity.User;
import java.util.List;

public record RegisterStoreServiceRequest(
        String name,
        String address,
        Double latitude,
        Double longitude,
        String ownerName,
        String ownerPhone,
        String businessNumber,
        Bank bankName,
        String bankAccount,
        List<StoreImagesRegister> storeImagesRegisters
) {
    public RegisterStoreServiceRequest(final String name, final String address, final Double latitude,
                                       final Double longitude, final String ownerName,
                                       final String ownerPhone, final String businessNumber, final Bank bankName,
                                       final String bankAccount,
                                       final List<StoreImagesRegister> storeImagesRegisters) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerName = ownerName;
        this.ownerPhone = validatePhone(ownerPhone);
        this.businessNumber = validateNumber(businessNumber);
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.storeImagesRegisters = validateImages(storeImagesRegisters);
    }

    private String validatePhone(final String value) {
        if (!value.matches("^(?!.*-)[0-9]{10,11}$")) {
            throw new InvalidRequestException(ErrorCode.USER_VALID_PHONE);
        }
        return value;
    }

    private String validateNumber(final String value) {
        if (!value.matches("^[0-9]+$")) {
            throw new InvalidRequestException(ErrorCode.INVALID_ONLY_NUMBER);
        }
        return value;
    }

    private List<StoreImagesRegister> validateImages(final List<StoreImagesRegister> value) {
        if (value.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.STORE_VALID_IMAGE);
        }
        return value;
    }

    public RegisterStoreDTO toStoreDTO(final Approved approved, final User user) {
        return new RegisterStoreDTO(name, address, latitude, longitude, ownerName, ownerPhone, businessNumber, bankName,
                bankAccount, storeImagesRegisters, approved, user);
    }
}
