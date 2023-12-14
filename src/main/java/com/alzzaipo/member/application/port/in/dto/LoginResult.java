package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.jwt.TokenInfo;
import lombok.Getter;

@Getter
public class LoginResult {

	private final boolean success;
	private final TokenInfo tokenInfo;

	public LoginResult(boolean success, TokenInfo tokenInfo) {
		this.success = success;
		this.tokenInfo = tokenInfo;
	}

	public static LoginResult getFailedResult() {
		return new LoginResult(false, null);
	}
}
