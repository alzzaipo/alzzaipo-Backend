package com.alzzaipo.email.application.port.in;

import com.alzzaipo.email.domain.EmailVerificationCode;
import com.alzzaipo.common.Email;

public interface VerifyEmailVerificationCodeUseCase {

    boolean verifyEmailVerificationCode(Email email, EmailVerificationCode verificationCode);
}
