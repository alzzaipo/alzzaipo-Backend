package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;

public interface ChangeLocalAccountPasswordPort {

    boolean changeLocalAccountPassword(LocalAccountId accountId, String encryptedNewAccountPassword);
}
