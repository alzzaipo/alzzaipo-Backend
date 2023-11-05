package com.alzzaipo.hexagonal.member.application.port.in.account.local;

import com.alzzaipo.hexagonal.member.domain.account.local.LocalAccountId;

public interface CheckLocalAccountIdAvailabilityQuery {

    boolean checkLocalAccountIdAvailability(LocalAccountId localAccountId);
}
