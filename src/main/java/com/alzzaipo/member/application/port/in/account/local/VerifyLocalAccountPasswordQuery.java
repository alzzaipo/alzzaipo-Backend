package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;

public interface VerifyLocalAccountPasswordQuery {

    boolean verifyLocalAccountPassword(Uid memberId, LocalAccountPassword password);
}
