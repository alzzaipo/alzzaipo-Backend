package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.domain.account.local.LocalAccountId;

public interface ChangeLocalAccountEmailPort {

    void changeLocalAccountEmail(LocalAccountId localAccountId, Email email);
}
