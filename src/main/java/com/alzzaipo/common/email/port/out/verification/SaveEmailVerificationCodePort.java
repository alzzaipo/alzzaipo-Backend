package com.alzzaipo.common.email.port.out.verification;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;

public interface SaveEmailVerificationCodePort {

	void save(String email, String emailVerificationCode, String subject, EmailVerificationPurpose purpose);
}
