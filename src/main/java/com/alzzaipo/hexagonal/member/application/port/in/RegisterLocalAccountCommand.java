package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountPassword;
import lombok.Getter;

@Getter
public class RegisterLocalAccountCommand {

    private final LocalAccountId localAccountId;
    private final LocalAccountPassword localAccountPassword;
    private final Email email;
    private final String nickname;

    public RegisterLocalAccountCommand(LocalAccountId localAccountId, LocalAccountPassword localAccountPassword, Email email, String nickname) {
        this.localAccountId = localAccountId;
        this.localAccountPassword = localAccountPassword;
        this.email = email;
        this.nickname = nickname;
    }
}
