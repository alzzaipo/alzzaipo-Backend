package com.alzzaipo.hexagonal.email.application.port.in;

import com.alzzaipo.hexagonal.email.domain.Email;

public interface SendEmailVerificationCodeUseCase {

    void sendEmailVerificationCode(Email email);
}
