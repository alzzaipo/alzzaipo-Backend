package com.alzzaipo.member.domain.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Uid;
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
