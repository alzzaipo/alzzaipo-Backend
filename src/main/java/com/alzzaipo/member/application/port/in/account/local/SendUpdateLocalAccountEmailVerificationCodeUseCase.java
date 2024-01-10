package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.email.domain.Email;

public interface SendUpdateLocalAccountEmailVerificationCodeUseCase {

    void sendUpdateLocalAccountEmailVerificationCode(Email email);
}
