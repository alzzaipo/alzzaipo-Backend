package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class UpdateMemberProfileCommand {

    @Valid
    private final Id memberId;

    @Valid
    private final Email email;

    private final String nickname;

    public UpdateMemberProfileCommand(Id memberId, String nickname, Email email) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
    }
}
