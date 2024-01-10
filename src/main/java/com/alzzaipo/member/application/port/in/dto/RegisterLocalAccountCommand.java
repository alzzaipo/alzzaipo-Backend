package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterLocalAccountCommand {

    private final LocalAccountId localAccountId;
    private final LocalAccountPassword localAccountPassword;
    private final Email email;
    private final String nickname;
    private final EmailVerificationCode emailVerificationCode;
}
