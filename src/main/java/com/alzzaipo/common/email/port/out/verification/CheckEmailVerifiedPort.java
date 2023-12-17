package com.alzzaipo.common.email.port.out.verification;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface CheckEmailVerifiedPort {

	boolean check(String email, String subject, EmailVerificationPurpose purpose);
}
