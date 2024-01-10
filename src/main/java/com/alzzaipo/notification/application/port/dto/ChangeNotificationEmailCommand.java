package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;

public class ChangeNotificationEmailCommand {

    private final Id memberId;
    private final Email email;
    private final EmailVerificationCode emailVerificationCode;

    public ChangeNotificationEmailCommand(Id memberId, String email, String emailVerificationCode) {
        this.memberId = memberId;
        this.email = new Email(email);
        this.emailVerificationCode = new EmailVerificationCode(emailVerificationCode);
    }

    public Id getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email.get();
    }

    public String getEmailVerificationCode() {
        return emailVerificationCode.get();
    }
}
