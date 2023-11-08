package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;
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
