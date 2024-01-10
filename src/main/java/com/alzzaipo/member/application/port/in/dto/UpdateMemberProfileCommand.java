package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import lombok.Getter;

@Getter
public class UpdateMemberProfileCommand {

    private final Id memberId;

    private final Email email;

    private final String nickname;

    private final EmailVerificationCode emailVerificationCode;

    public UpdateMemberProfileCommand(Id memberId, String nickname, Email email, String emailVerificationCode) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.emailVerificationCode = new EmailVerificationCode(emailVerificationCode);
    }
}
