package com.alzzaipo.common.email.domain;

import com.alzzaipo.common.exception.CustomException;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class EmailVerificationCode {

    public static final int length = 8;
    private static final String regex = "^[A-Za-z0-9]{8}$";

    private String emailVerificationCode;

    public EmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
        validateFormat(emailVerificationCode);
    }

    private void validateFormat(String emailVerificationCode) {
        if (emailVerificationCode.length() != length || !Pattern.matches(regex, emailVerificationCode)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "인증코드 형식 오류");
        }
    }

    public String get() {
        return emailVerificationCode;
    }
}
