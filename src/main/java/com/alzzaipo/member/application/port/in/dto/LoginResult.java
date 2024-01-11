package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.token.domain.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResult {

	private final boolean success;
	private final boolean captchaRequired;
	private final TokenInfo tokenInfo;

	public static LoginResult createInvalidCaptchaResponseResult() {
		return new LoginResult(false, true, null);
	}

	public static LoginResult createInvalidCredentialsResult() {
		return new LoginResult(false, false, null);
	}

	public static LoginResult createSuccessResult(TokenInfo tokenInfo) {
		return new LoginResult(true, false, tokenInfo);
	}
}
