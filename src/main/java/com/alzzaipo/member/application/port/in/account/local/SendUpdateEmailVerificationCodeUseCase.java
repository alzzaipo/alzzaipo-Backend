package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Uid;

public interface SendUpdateEmailVerificationCodeUseCase {

	void send(Email email, Uid memberId);
}
