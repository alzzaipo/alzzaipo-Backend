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
    ACCOUNT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    STOCK_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_KAKAO_AUTH_CODE(HttpStatus.BAD_REQUEST, ""),
    INVALID_KAKAO_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, ""),
    KAKAO_ACCOUNT_NOT_REGISTERED(HttpStatus.UNAUTHORIZED, ""),
    IPO_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    PORTFOLIO_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, ""),
    EXPIRED_EMAIL_AUTHENTICATION_CODE(HttpStatus.NOT_FOUND, ""),
    INCORRECT_EMAIL_AUTHENTICATION_CODE(HttpStatus.UNAUTHORIZED, ""),
    INVALID_EMAIL_ADDRESS_FORMAT(HttpStatus.BAD_REQUEST, ""),
    AUTHENTICATION_CODE_SEND_FAILED(HttpStatus.BAD_REQUEST, ""),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "");


    private final HttpStatus httpStatus;
    private final String message;
}
