package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class SendSignUpEmailVerificationCodeCommand {

	@Valid
	private final LocalAccountId accountId;

	@Valid
	private final Email email;

	public SendSignUpEmailVerificationCodeCommand(String accountId, String email) {
		this.accountId = new LocalAccountId(accountId);
		this.email = new Email(email);
	}
}
