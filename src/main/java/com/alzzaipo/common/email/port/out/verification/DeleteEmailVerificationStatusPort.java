package com.alzzaipo.common.email.port.out.verification;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface DeleteEmailVerificationStatusPort {

	void delete(String email, EmailVerificationPurpose purpose);
}
