package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;

public class CheckNotificationEmailVerificationCodeCommand {

    private final Email email;
    private final EmailVerificationCode emailVerificationCode;

    public CheckNotificationEmailVerificationCodeCommand(String email, String emailVerificationCode) {
        this.email = new Email(email);
        this.emailVerificationCode = new EmailVerificationCode(emailVerificationCode);
    }

    public String getEmail() {
        return email.get();
    }

    public String getEmailVerificationCode() {
        return emailVerificationCode.get();
    }
}
