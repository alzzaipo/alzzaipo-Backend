package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountEmailAvailablePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckLocalAccountEmailAvailableService implements CheckLocalAccountEmailAvailabilityQuery {

	private final CheckLocalAccountEmailAvailablePort checkLocalAccountEmailAvailablePort;

	@Override
	public boolean check(@Valid Email email) {
		return checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get());
	}
}
