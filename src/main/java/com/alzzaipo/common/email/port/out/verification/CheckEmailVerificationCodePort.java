package com.alzzaipo.common.email.port.out.verification;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface CheckEmailVerificationCodePort {

    boolean check(Email email, EmailVerificationCode verificationCode, EmailVerificationPurpose purpose);
}
