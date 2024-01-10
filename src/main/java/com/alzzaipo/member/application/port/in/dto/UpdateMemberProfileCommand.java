package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMemberProfileCommand {

    private final Id memberId;
    private final Email email;
    private final String nickname;
    private final EmailVerificationCode emailVerificationCode;
}
