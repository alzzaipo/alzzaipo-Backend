package com.alzzaipo.hexagonal.member.domain.LocalAccount;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LocalAccount {

    private final UUID memberUUID;
    private final LocalAccountId accountId;
    private final LocalAccountPassword accountPassword;
    private final String email;

    public LocalAccount(UUID memberUUID, LocalAccountId accountId, LocalAccountPassword accountPassword, String email) {
        this.memberUUID = memberUUID;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.email = email;
    }
}
