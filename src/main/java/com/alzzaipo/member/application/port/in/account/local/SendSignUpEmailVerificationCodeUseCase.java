package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.application.port.in.dto.SendSignUpEmailVerificationCodeCommand;

public interface SendSignUpEmailVerificationCodeUseCase {

    void sendSignUpEmailVerificationCode(SendSignUpEmailVerificationCodeCommand command);
}
