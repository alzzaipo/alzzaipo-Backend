package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.domain.account.local.LocalAccountId;

public interface CheckLocalAccountIdAvailableQuery {

    boolean checkAccountIdAvailable(LocalAccountId localAccountId);
}
