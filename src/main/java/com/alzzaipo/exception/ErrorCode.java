package com.alzzaipo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, ""),
    ACCOUNT_ID_DUPLICATED(HttpStatus.CONFLICT, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    ACCOUNT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "");

    private HttpStatus httpStatus;
    private String message;
}
