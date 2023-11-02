package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;

public interface CheckLocalAccountIdAvailabilityUseCase {

    boolean checkLocalAccountIdAvailability(LocalAccountId accountId);
}
