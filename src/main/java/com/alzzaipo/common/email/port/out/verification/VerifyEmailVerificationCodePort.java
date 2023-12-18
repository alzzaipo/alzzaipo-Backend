package com.alzzaipo.common.email.port.out.verification;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface VerifyEmailVerificationCodePort {

	boolean verify(String verificationCode, EmailVerificationPurpose purpose);
}
