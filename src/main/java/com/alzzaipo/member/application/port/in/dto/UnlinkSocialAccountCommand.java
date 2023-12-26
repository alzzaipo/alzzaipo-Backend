package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class UnlinkSocialAccountCommand {

    private final Id memberId;
    private final LoginType loginType;

    public UnlinkSocialAccountCommand(Id memberId, LoginType loginType) {
        this.memberId = memberId;
        this.loginType = loginType;
    }
}
