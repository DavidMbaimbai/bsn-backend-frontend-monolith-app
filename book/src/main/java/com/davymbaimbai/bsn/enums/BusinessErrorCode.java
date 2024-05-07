package com.davymbaimbai.bsn.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCode {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "The current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new  password does not match"),
    ACCOUNT_LOCKED(301, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(302, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIAL(303, FORBIDDEN, "Username and / or password is incorrect"),

    ;


    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCode(int code , HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;

    }
}
