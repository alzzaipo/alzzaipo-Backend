package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckLocalAccountEmailVerificationCodeCommand {

    private final Email email;
    private final EmailVerificationCode verificationCode;
    private final EmailVerificationPurpose emailVerificationPurpose;
}
