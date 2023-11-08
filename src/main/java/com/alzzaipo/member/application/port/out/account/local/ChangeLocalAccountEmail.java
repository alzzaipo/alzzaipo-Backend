package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.Email;
import com.alzzaipo.member.domain.account.local.LocalAccountId;

public interface ChangeLocalAccountEmail {

    void changeLocalAccountEmail(LocalAccountId localAccountId, Email email);
}
