package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public class CheckLocalAccountEmailVerificationCodeCommand {

    private final Email email;
    private final EmailVerificationCode verificationCode;
    private final EmailVerificationPurpose emailVerificationPurpose;

    public CheckLocalAccountEmailVerificationCodeCommand(String email, String verificationCode,
        EmailVerificationPurpose emailVerificationPurpose) {
        this.email = new Email(email);
        this.verificationCode = new EmailVerificationCode(verificationCode);
        this.emailVerificationPurpose = emailVerificationPurpose;
    }

    public String getEmail() {
        return email.get();
    }

    public String getVerificationCode() {
        return verificationCode.get();
    }

    public EmailVerificationPurpose getEmailVerificationPurpose() {
        return emailVerificationPurpose;
    }
}
