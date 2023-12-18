package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountIdAvailablePort;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckLocalAccountIdAvailabilityService implements CheckLocalAccountIdAvailabilityQuery {

	private final CheckLocalAccountIdAvailablePort checkLocalAccountIdAvailablePort;

	@Override
	public boolean check(@Valid LocalAccountId localAccountId) {
		return checkLocalAccountIdAvailablePort.checkAccountIdAvailable(localAccountId.get());
	}
}
