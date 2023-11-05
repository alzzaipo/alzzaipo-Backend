package com.alzzaipo.hexagonal.member.application.port.in.dto;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import lombok.Getter;

@Getter
public class UpdateMemberProfileCommand {

    private final Uid memberUID;
    private final String nickname;
    private final Email email;

    public UpdateMemberProfileCommand(Uid memberUID, String nickname, Email email) {
        this.memberUID = memberUID;
        this.nickname = nickname;
        this.email = email;
    }
}
