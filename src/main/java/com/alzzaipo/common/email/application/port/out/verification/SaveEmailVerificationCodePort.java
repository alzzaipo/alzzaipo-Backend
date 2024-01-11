package com.alzzaipo.common.email.application.port.out.verification;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface SaveEmailVerificationCodePort {

    void save(Email email, EmailVerificationCode emailVerificationCode, EmailVerificationPurpose purpose);
}
