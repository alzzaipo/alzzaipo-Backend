package com.alzzaipo.util;

import com.alzzaipo.exception.AppException;
import com.alzzaipo.enums.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataFormatUtil {

    public static boolean checkEmailFormat(String email) {
        Pattern pattern = Pattern.compile("^(?=.{1,256}$)[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static void validateEmailFormat(String email) {
        if(!checkEmailFormat(email)) {
            throw new AppException(ErrorCode.INVALID_EMAIL_FORMAT, "올바르지 않은 이메일 형식입니다 - " + email);
        }
    }

    public static boolean checkVerificationCodeFormat(String verificationCode) {
        return Pattern.matches("^[a-zA-Z0-9]{8}$", verificationCode);
    }

    public static void validateVerificationCodeFormat(String verificationCode) {
        if(!checkVerificationCodeFormat(verificationCode)) {
            throw new AppException(ErrorCode.INVALID_EMAIL_VERIFICATION_CODE, "올바르지 않은 인증코드 형식 입니다.");
        }
    }

    public static boolean checkAccountIdFormat(String accountId) {
        String regex = "^[a-z0-9_-]{5,20}$";
        return Pattern.matches(regex, accountId);
    }

    public static void validateAccountIdFormat(String accountId) {
        if(!checkAccountIdFormat(accountId)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "올바르지 않은 아이디 형식입니다.");
        }
    }

    public static boolean checkAccountPasswordFormat(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?/]).{8,16}$";
        return Pattern.matches(regex, password);
    }

    public static void validateAccountPasswordFormat(String password) {
        if(!checkAccountPasswordFormat(password)) {
            throw new AppException(ErrorCode.INVALID_PASSWORD_FORMAT, "올바르지 않은 비밀번호 형식입니다.");
        }
    }
}
