package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;

public interface SendEmailVerificationCodePort {

    EmailVerificationCode sendEmailVerificationCode(Email email);
}
