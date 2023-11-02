package com.alzzaipo.hexagonal.email.application.port.in;

import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;
import com.alzzaipo.hexagonal.email.domain.Email;

public interface VerifyEmailVerificationCodeUseCase {

    boolean verifyEmailVerificationCode(Email email, EmailVerificationCode verificationCode);
}
