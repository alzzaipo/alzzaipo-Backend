package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.member.domain.account.local.LocalAccountId;

public interface ChangeLocalAccountPasswordPort {

    boolean changeLocalAccountPassword(LocalAccountId accountId, String encryptedNewAccountPassword);
}
