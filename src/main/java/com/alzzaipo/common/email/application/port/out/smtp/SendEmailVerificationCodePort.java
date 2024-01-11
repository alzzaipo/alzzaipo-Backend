package com.alzzaipo.common.email.application.port.out.smtp;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;

public interface SendEmailVerificationCodePort {

	EmailVerificationCode sendVerificationCode(Email email);
}
