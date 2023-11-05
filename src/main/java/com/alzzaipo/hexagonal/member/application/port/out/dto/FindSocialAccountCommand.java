package com.alzzaipo.hexagonal.member.application.port.out.dto;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.LoginType;
import lombok.Getter;

@Getter
public class FindSocialAccountCommand {

    private final LoginType loginType;
    private final Email email;

    public FindSocialAccountCommand(LoginType loginType, Email email) {
        this.loginType = loginType;
        this.email = email;
    }
}
