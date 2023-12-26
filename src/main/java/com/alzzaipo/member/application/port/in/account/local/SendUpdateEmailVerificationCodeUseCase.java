package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;

public interface SendUpdateEmailVerificationCodeUseCase {

	void sendUpdateEmailVerificationCode(Email email, Id memberId);
}
