package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
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