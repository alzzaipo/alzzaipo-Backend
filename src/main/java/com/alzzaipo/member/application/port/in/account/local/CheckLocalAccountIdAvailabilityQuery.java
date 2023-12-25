package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.domain.account.local.LocalAccountId;

public interface CheckLocalAccountIdAvailabilityQuery {

    boolean checkAccountIdAvailable(LocalAccountId localAccountId);
}
