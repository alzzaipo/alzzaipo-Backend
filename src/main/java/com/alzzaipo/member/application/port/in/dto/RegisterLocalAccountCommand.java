package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Email;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.Getter;

@Getter
public class RegisterLocalAccountCommand {

    private final LocalAccountId localAccountId;
    private final LocalAccountPassword localAccountPassword;
    private final Email email;
    private final String nickname;

    public RegisterLocalAccountCommand(String accountId, String accountPassword, String email, String nickname) {
        this.localAccountId = new LocalAccountId(accountId);
        this.localAccountPassword = new LocalAccountPassword(accountPassword);
        this.email = new Email(email);
        this.nickname = nickname;
    }
}
