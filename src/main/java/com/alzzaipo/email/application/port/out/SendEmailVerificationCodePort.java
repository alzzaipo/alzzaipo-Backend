package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;
import com.alzzaipo.email.domain.EmailVerificationCode;

public interface SendEmailVerificationCodePort {

    EmailVerificationCode sendEmailVerificationCode(Email email);
}
