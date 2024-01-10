package com.alzzaipo.common.email.domain;

import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
public class EmailVerificationCode {

    public static final int length = 8;

    @Length(min = length, max = length)
    @Pattern(message = "이메일 형식 오류", regexp = "^[A-Za-z0-9]{8}$")
    private String emailVerificationCode;

    public EmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }

    public String get() {
        return emailVerificationCode;
    }
}
