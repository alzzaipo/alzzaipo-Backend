package com.alzzaipo.hexagonal.member.domain.account.local;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import lombok.Getter;

@Getter
public class LocalAccount {

    private final Uid memberUID;
    private final LocalAccountId accountId;
    private final LocalAccountPassword accountPassword;
    private final Email email;

    public LocalAccount(Uid memberUID, LocalAccountId accountId, LocalAccountPassword accountPassword, Email email) {
        this.memberUID = memberUID;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.email = email;
    }
}
