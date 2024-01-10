package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.member.domain.account.local.LocalAccountId;

public interface CheckLocalAccountIdAvailablePort {

	boolean checkAccountIdAvailable(LocalAccountId accountId);

}
