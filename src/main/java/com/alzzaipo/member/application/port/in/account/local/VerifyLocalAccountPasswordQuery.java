package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.Id;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;

public interface VerifyLocalAccountPasswordQuery {

    boolean verifyLocalAccountPassword(Id memberId, LocalAccountPassword password);
}
