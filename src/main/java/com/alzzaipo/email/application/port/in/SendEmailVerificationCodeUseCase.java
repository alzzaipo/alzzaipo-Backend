package com.alzzaipo.email.application.port.in;

import com.alzzaipo.common.Email;

public interface SendEmailVerificationCodeUseCase {

    void sendEmailVerificationCode(Email email);
}
