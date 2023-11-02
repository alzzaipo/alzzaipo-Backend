package com.alzzaipo.hexagonal.email.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailVerificationCode {

    public static final int length = 8;

    private final String emailVerificationCode;

    public EmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;

        validateFormat();
    }

    private void validateFormat() {
        String regex = "^[A-Za-z0-9]{8}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailVerificationCode);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("이메일 인증코드 형식 오류");
        }
    }

    public String get() {
        return emailVerificationCode;
    }
}
