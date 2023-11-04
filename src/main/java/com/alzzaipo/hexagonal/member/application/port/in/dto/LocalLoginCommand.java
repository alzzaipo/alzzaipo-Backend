package com.alzzaipo.hexagonal.member.application.port.in.dto;

import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountPassword;
import lombok.Getter;

@Getter
public class LocalLoginCommand {

    private final LocalAccountId localAccountId;
    private final LocalAccountPassword localAccountPassword;

    public LocalLoginCommand(String accountId, String accountPassword) {
        this.localAccountId = new LocalAccountId(accountId);
        this.localAccountPassword = new LocalAccountPassword(accountPassword);
    }
}
