package com.alzzaipo.common.email.port.out.verification;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface CheckEmailVerificationCodePort {

    boolean check(String email, String verificationCode, EmailVerificationPurpose purpose);
}
