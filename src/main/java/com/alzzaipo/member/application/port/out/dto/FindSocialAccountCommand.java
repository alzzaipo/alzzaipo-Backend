package com.alzzaipo.member.application.port.out.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindSocialAccountCommand {

    private final LoginType loginType;
    private final Email email;
}
