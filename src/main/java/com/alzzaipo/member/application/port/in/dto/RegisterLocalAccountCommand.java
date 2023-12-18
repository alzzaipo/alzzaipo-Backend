package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.member.adapter.in.web.dto.RegisterLocalAccountWebRequest;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class RegisterLocalAccountCommand {

	@Valid
	private final LocalAccountId localAccountId;

	@Valid
	private final LocalAccountPassword localAccountPassword;

	@Valid
	private final Email email;

	private final String nickname;

	public RegisterLocalAccountCommand(String accountId, String accountPassword, String email, String nickname) {
		this.localAccountId = new LocalAccountId(accountId);
		this.localAccountPassword = new LocalAccountPassword(accountPassword);
		this.email = new Email(email);
		this.nickname = nickname;
	}

	public static RegisterLocalAccountCommand build(RegisterLocalAccountWebRequest dto) {
		return new RegisterLocalAccountCommand(dto.getAccountId(),
			dto.getAccountPassword(),
			dto.getEmail(),
			dto.getNickname());
	}
}
