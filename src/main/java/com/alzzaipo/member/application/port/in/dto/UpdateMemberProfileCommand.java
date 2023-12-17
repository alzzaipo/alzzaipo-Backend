package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Uid;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class UpdateMemberProfileCommand {

    @Valid
    private final Uid memberUID;

    @Valid
    private final Email email;

    private final String nickname;

    public UpdateMemberProfileCommand(Uid memberUID, String nickname, Email email) {
        this.memberUID = memberUID;
        this.nickname = nickname;
        this.email = email;
    }
}
