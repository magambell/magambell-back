package com.magambell.server.common.utility;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtility {

    public static String encodePassword(String password) {
        validatePassword(password);
        return new BCryptPasswordEncoder().encode(password);
    }

    private static void validatePassword(final String password) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$")) {
            throw new InvalidRequestException(ErrorCode.USER_VALID_PASSWORD);
        }
    }
}
