package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.email.domain.Email;

public interface CheckLocalAccountEmailAvailablePort {

	boolean checkEmailAvailable(Email email);

}
