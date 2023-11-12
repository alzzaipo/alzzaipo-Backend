package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class UnlinkSocialAccountCommand {

    private final Uid memberUID;
    private final LoginType loginType;

    public UnlinkSocialAccountCommand(Uid memberUID, LoginType loginType) {
        this.memberUID = memberUID;
        this.loginType = loginType;
    }
}
