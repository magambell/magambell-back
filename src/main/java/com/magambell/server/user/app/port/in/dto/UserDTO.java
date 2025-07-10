package com.magambell.server.user.app.port.in.dto;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;

public record UserDTO(String email, String password, String name, String phoneNumber, UserRole userRole) {

    public UserDTO(final String email, final String password, final String name, final String phoneNumber,
                   final UserRole userRole) {
        this.email = validateEmail(email);
        this.password = password;
        this.name = name;
        this.phoneNumber = validatePhone(phoneNumber);
        this.userRole = userRole;
    }

    private String validateEmail(final String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidRequestException(ErrorCode.USER_VALID_EMAIL);
        }
        return email;
    }

    private String validatePhone(final String phoneNumber) {
        if (!phoneNumber.matches("^(?!.*-)[0-9]{10,11}$")) {
//            throw new InvalidRequestException(ErrorCode.USER_VALID_PHONE);
        }
        return phoneNumber;
    }

    public User toUser() {
        return User.create(this);
    }
}
