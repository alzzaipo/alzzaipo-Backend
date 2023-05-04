package com.alzzaipo.config;

import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    public static void verifyEmailFormat(String email) {
        // 이메일 형식 검사
        Pattern pattern = Pattern.compile("^(?=.{1,256}$)[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.find()) {
            throw new AppException(ErrorCode.INVALID_EMAIL_FORMAT, "올바르지 않은 이메일 형식입니다 - " + email);
        }
    }

    public static void validateVerificationCodeFormat(String verificationCode) {
        if(!Pattern.matches("^[a-zA-Z0-9]{8}$", verificationCode)) {
            throw new AppException(ErrorCode.INVALID_EMAIL_VERIFICATION_CODE, "올바르지 않은 인증코드 형식 입니다.");
        }
    }

}
