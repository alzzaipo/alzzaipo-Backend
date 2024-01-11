package com.alzzaipo.common.email.application.port.out.verification;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface DeleteEmailVerificationStatusPort {

	void delete(Email email, EmailVerificationPurpose purpose);
}
