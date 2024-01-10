package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.application.port.in.dto.CheckLocalAccountEmailVerificationCodeCommand;

public interface CheckEmailVerificationCodeQuery {

    boolean checkEmailVerificationCode(CheckLocalAccountEmailVerificationCodeCommand command);
}
