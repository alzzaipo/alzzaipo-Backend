package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnlinkSocialAccountCommand {

    private final Id memberId;
    private final LoginType loginType;
}
