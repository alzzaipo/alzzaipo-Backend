package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.domain.EmailVerificationCode;

public interface VerifyEmailVerificationCodeUseCase {

    boolean verify(EmailVerificationCode verificationCode, EmailVerificationPurpose purpose);
}
